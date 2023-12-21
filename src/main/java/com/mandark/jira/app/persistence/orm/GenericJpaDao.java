package com.mandark.jira.app.persistence.orm;

import static com.mandark.jira.app.persistence.orm.JpaEntity.FIELD_ID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.persistence.IEntity;
import com.mandark.jira.spi.app.persistence.QueryBuilder;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.query.InCriteria;
import com.mandark.jira.spi.app.query.OrderBy;
import com.mandark.jira.spi.lang.ObjectNotFoundException;


/**
 * Data Access Object for all the Entities implementing {@link IEntity}.
 * 
 * <p>
 * This Data Access Object(DAO) provides the methods like persist, update, find/search .. to
 * manipulate these objects.
 * </p>
 * 
 * <p>
 * This implementation makes use of the JPA ( Javax Persistence API ) for its persistence context
 * and {@link EntityManager}.
 * </p>
 * 
 * <p>
 * Be vary of transaction management while using this object.
 * </p>
 * 
 * @param E extends {@link IEntity}
 * @param K extends {@link Serializable}
 * 
 * @see IDao
 * @see IEntity
 */
@SuppressWarnings({"unchecked"})
public class GenericJpaDao implements IDao<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericJpaDao.class);

    protected EntityManager entityManager;
    protected QueryBuilder<Query> queryBuilder;

    private int batchSize;


    // Getters and Setters
    // ------------------------------------------------------------------------

    /**
     * Returns the entity manager
     * 
     * @return the entity manager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Sets the entity manager
     * 
     * @param entityManager
     */
    @PersistenceContext(unitName = "punit")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.entityManager.setFlushMode(FlushModeType.COMMIT);

        // init QueryBuilder
        // TODO move it to appCtx XML
        this.queryBuilder = new JpaQueryBuilder(entityManager);
    }


    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }


    // Standard DAO methods
    // ------------------------------------------------------------------------


    // Create

    @Override
    public Integer save(final IEntity<Integer> inEntity) {
        // Sanity checks
        if (Objects.isNull(inEntity)) {
            throw new IllegalArgumentException("#save :: in Entity object is NULL");
        }

        entityManager.persist(inEntity);
        entityManager.flush();

        final Integer pKey = inEntity.getId();
        return pKey;
    }

    @Override
    public List<Integer> save(final Collection<? extends IEntity<Integer>> inEntities) {
        // Sanity checks
        if (Objects.isNull(inEntities) || inEntities.isEmpty()) {
            LOGGER.debug("#save(Collection) :: Empty collection of entities passed.");
            return new ArrayList<>();
        }

        final List<Integer> pKeys = new ArrayList<>();

        // Iterate and persist
        int i = 0;
        for (IEntity<Integer> e : inEntities) {
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }

            entityManager.persist(e);
            i++;

            final Integer pKey = e.getId();
            pKeys.add(pKey);
        }

        return pKeys;
    }


    // Update

    @Override
    public void update(final Integer id, final IEntity<Integer> inEntity) {
        // Sanity checks
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("#update :: in Entity ID is NULL");
        }

        if (Objects.isNull(inEntity)) {
            throw new IllegalArgumentException("#update :: in Entity is NULL");
        }

        // Update
        inEntity.setId(id);
        entityManager.merge(inEntity);
    }

    @Override
    public void update(final Collection<? extends IEntity<Integer>> inEntities) {
        // Sanity checks
        if (Objects.isNull(inEntities) || inEntities.isEmpty()) {
            LOGGER.debug("#update(Collection) :: Empty collection of entities passed.");
            return;
        }

        // Iterate and merge
        int i = 0;
        for (IEntity<Integer> e : inEntities) {
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }

            entityManager.merge(e);
        }
    }


    // Purge

    @Override
    public void purge(final Class<? extends IEntity<Integer>> entityCls, final Integer id) {
        // Sanity checks
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("#purge :: in Entity ID is BLANK");
        }

        // Read
        final IEntity<Integer> entity = this.read(entityCls, id, false);
        if (Objects.isNull(entity)) {
            return;
        }

        entityManager.remove(entity);
    }

    @Override
    public void purge(final Class<? extends IEntity<Integer>> entityCls, final Collection<Integer> ids) {
        // Sanity checks
        if (Objects.isNull(ids) || ids.isEmpty()) {
            LOGGER.debug("#purge(Collection) :: Empty collection of IDs passed.");
            return;
        }

        // Read
        final List<? extends IEntity<Integer>> entities = this.read(entityCls, ids);

        // Iterate and purge
        int i = 0;
        for (final IEntity<Integer> e : entities) {
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }

            entityManager.remove(e);
            i++;
        }
    }


    // Read
    // ------------------------------------------------------------------------

    @Override
    public <E extends IEntity<Integer>> E read(final Class<E> entityCls, final Integer inPkey,
            final boolean errorIfNotFound) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#read :: in Entity Class is NULL");
        }

        if (Objects.isNull(inPkey)) {
            throw new IllegalArgumentException("#purge :: in Entity ID is BLANK");
        }

        // Entity Obj
        final E entityObj = entityManager.find(entityCls, inPkey);

        // non-null
        if (errorIfNotFound && Objects.isNull(entityObj)) {
            String errMsg = String.format("No Entity Object found with the passed ID : %s", inPkey);
            LOGGER.error(errMsg);
            throw new ObjectNotFoundException(errMsg);
        }

        return entityObj;
    }

    @Override
    public <E extends IEntity<Integer>> List<E> read(final Class<E> entityCls, final Collection<Integer> inPkeys) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#read :: in Entity Class is NULL");
        }

        if (CollectionUtils.isEmpty(inPkeys)) {
            LOGGER.debug("#read(Collection) :: input IDs collection is EMPTY");
            return new ArrayList<>();
        }

        // Criteria
        final InCriteria criteria = Criteria.in(FIELD_ID, inPkeys);

        // Query
        final Query query = queryBuilder.toQuery(entityCls, criteria, (OrderBy) null);
        LOGGER.debug("#read (JPA Query) :: {}", query);

        // Result
        final List<E> resultList = (List<E>) query.getResultList();
        return resultList;
    }



    @Override
    public <E extends IEntity<Integer>> int count(final Class<E> entityCls) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#count :: in Entity Class is NULL");
        }

        // Query
        final Query query = queryBuilder.toCountQuery(entityCls);
        LOGGER.debug("#count (JPA Query) :: {}", query);

        // Result
        final Long count = (Long) query.getSingleResult();
        return count.intValue();
    }

    @Override
    public <E extends IEntity<Integer>> List<E> read(final Class<E> entityCls, final int pageNo, final int pageSize) {
        return this.read(entityCls, (OrderBy) null, pageNo, pageSize);
    }

    @Override
    public <E extends IEntity<Integer>> List<E> read(final Class<E> entityCls, final OrderBy orderby, final int pageNo,
            final int pageSize) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#read :: in Entity Class is NULL");
        }

        // Query
        final Query query = queryBuilder.toQuery(entityCls);
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);

        LOGGER.debug("#read (JPA Query) :: {}", query);

        // Result
        final List<E> resultList = (List<E>) query.getResultList();
        return resultList;
    }

    @Override
    public <E extends IEntity<Integer>> List<E> read(Class<E> entityCls, List<OrderBy> orderbyList, int pageNo,
            int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }



    // Find

    @Override
    public <E extends IEntity<Integer>> int count(Class<E> entityCls, Criteria criteria) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#count :: in Entity Class is NULL");
        }

        // Query
        final Query query = queryBuilder.toCountQuery(entityCls, criteria);
        LOGGER.debug("#count (JPA Query) :: {}", query);

        // Result
        final Long count = (Long) query.getSingleResult();
        return count.intValue();
    }

    @Override
    public <E extends IEntity<Integer>> List<E> find(final Class<E> entityCls, final Criteria criteria,
            final int pageNo, final int pageSize) {
        return this.find(entityCls, criteria, (OrderBy) null, pageNo, pageSize);
    }

    @Override
    public <E extends IEntity<Integer>> List<E> find(final Class<E> entityCls, final Criteria criteria,
            final OrderBy orderBy, final int pageNo, final int pageSize) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#find :: in Entity Class is NULL");
        }

        // Query
        final Query query = queryBuilder.toQuery(entityCls, criteria, orderBy);
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);

        LOGGER.debug("#read (JPA Query) :: {}", query);

        // Result
        final List<E> resultList = (List<E>) query.getResultList();
        return resultList;
    }

    @Override
    public <E extends IEntity<Integer>> List<E> find(Class<E> entityCls, Criteria criteria, List<OrderBy> orderbyList,
            int pageNo, int pageSize) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#find :: in Entity Class is NULL");
        }

        // Query
        final Query query = queryBuilder.toQuery(entityCls, criteria, orderbyList);
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);

        LOGGER.debug("#read (JPA Query) :: {}", query);

        // Result
        final List<E> resultList = (List<E>) query.getResultList();
        return resultList;
    }

    // Other Utilities
    // ------------------------------------------------------------------------

    @Override
    public void truncate(Class<? extends IEntity<Integer>> entityCls) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#truncate :: in Entity Class is NULL");
        }

        // Table Name
        final Table tableAnnotation = entityCls.getAnnotation(Table.class);

        // Build Native Query
        final StringBuilder querySB = new StringBuilder();
        querySB.append("TRUNCATE TABLE ");
        querySB.append(tableAnnotation.name());

        // Execute Query
        final String query = querySB.toString();
        entityManager.createNativeQuery(query).executeUpdate();
    }


}
