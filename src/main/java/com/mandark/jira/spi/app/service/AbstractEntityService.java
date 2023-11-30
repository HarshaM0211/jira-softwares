package com.mandark.jira.spi.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.spi.app.EntityBean;
import com.mandark.jira.spi.app.EntityDTO;
import com.mandark.jira.spi.app.SearchQuery;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.persistence.IEntity;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.query.OrderBy;
import com.mandark.jira.spi.lang.NotImplementedException;
import com.mandark.jira.spi.lang.ObjectNotFoundException;


abstract class AbstractEntityService<K, E extends IEntity<K>, EB extends EntityBean<E>, ED extends EntityDTO<E>> //
        implements EntityService<K, E, ED> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityService.class);

    protected final IDao<K> dao;


    // Constructor
    // ------------------------------------------------------------------------

    AbstractEntityService(IDao<K> dao) {
        super();

        // init
        this.dao = dao;
    }


    // Abstract Methods
    // ------------------------------------------------------------------------

    protected abstract Class<E> getEntityClass();

    protected abstract ED toDTO(E entityObj);


    protected abstract E createFromBean(EB bean);

    protected abstract E copyFromBean(E exEntity, EB entityBean);


    // CRUD

    protected abstract K save(EB entityBean);

    protected abstract List<K> save(Collection<EB> entityBeans);


    protected abstract void update(K id, EB entityBean);


    protected abstract void purge(K id);

    protected abstract void purge(Collection<K> idsList);



    // Protected Methods
    // ------------------------------------------------------------------------

    protected String getEntityName() {
        return this.getEntityClass().getSimpleName();
    }

    protected List<ED> toDTOs(Collection<E> entityObjs) {
        // Sanity checks
        if (Objects.isNull(entityObjs) || entityObjs.isEmpty()) {
            return new ArrayList<>();
        }

        return entityObjs.stream().map(e -> this.toDTO(e)).collect(Collectors.toList());
    }


    // Entity

    protected <IE extends IEntity<K>> IE readEntity(final Class<IE> entityCls, final K id,
            final boolean errorIfNotFound) {
        // Sanity checks
        if (Objects.isNull(id)) {
            return null;
        }

        // Read
        final IE entityObj = this.dao.read(entityCls, id, false);
        if (errorIfNotFound && Objects.isNull(entityObj)) {
            String entityName = this.getEntityName();
            String errMsg = String.format("#readEntity :: No Object found with id : %s - %s", entityName, id);
            String errMsgUser = String.format("Oops! %s resource you're looking for is not found.", entityName);
            throw new ObjectNotFoundException(errMsg, errMsgUser);
        }

        return entityObj;
    }

    protected <IE extends IEntity<K>> Map<K, IE> readEntities(final Class<IE> entityCls, final Collection<K> inIds) {
        // Sanity checks
        if (Objects.isNull(inIds) || inIds.isEmpty()) {
            return new HashMap<>();
        }

        // Read :: IDs
        final Set<K> ids = inIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        final List<IE> entityObjs = this.dao.read(entityCls, ids);
        if (Objects.isNull(entityObjs) || entityObjs.isEmpty()) {
            return new HashMap<>();
        }

        // asMap
        final Map<K, IE> entityIdMap = entityObjs.stream() //
                .filter(Objects::nonNull) //
                .collect(Collectors.toMap(IE::getId, Function.identity()));

        return entityIdMap;
    }

    // Find

    protected int count(Criteria criteria) {
        // Sanity checks
        if (Objects.isNull(criteria)) {
            throw new IllegalArgumentException("#count :: entity Criteria is NULL");
        }

        // Count
        final int count = this.dao.count(this.getEntityClass(), criteria);
        LOGGER.debug("# of entity objects found for critera :: {} - {} : {}", this.getEntityClass(), criteria, count);

        return count;
    }

    protected List<ED> find(Criteria criteria, int pageNo, int pageSize) {
        return this.find(criteria, null, pageNo, pageSize);
    }

    protected List<ED> find(Criteria criteria, OrderBy orderBy, int pageNo, int pageSize) {
        // Sanity checks
        if (Objects.isNull(criteria)) {
            throw new IllegalArgumentException("#find :: entity Criteria is NULL");
        }

        // Find
        final List<E> entityObjs = this.dao.find(this.getEntityClass(), criteria, orderBy, pageNo, pageSize);
        return this.toDTOs(entityObjs);
    }

    // Search

    protected Criteria asCriteria(final SearchQuery<? extends E> searchQuery) {
        final String errMsg = String.format("Search is not Implemented for : %s", this.getEntityName());
        LOGGER.error(errMsg);
        throw new NotImplementedException(errMsg);
    }


    // EntityService Methods
    // ------------------------------------------------------------------------

    @Override
    public ED read(final K id) {
        // Sanity checks
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("#read :: Entity ID is BLANK");
        }

        // entity
        final E entityObj = dao.read(this.getEntityClass(), id, false);

        // toDTO
        final ED entityDTO = this.toDTO(entityObj);
        return entityDTO;
    }

    @Override
    public ED read(final K id, final boolean errorIfNotFound) {
        // Read
        final ED entityObj = this.read(id);

        // non-null
        if (Objects.isNull(entityObj) && errorIfNotFound) {
            String errMsg = String.format("No Entity Object found with the passed ID : %s", id);
            LOGGER.error(errMsg);
            throw new ObjectNotFoundException(errMsg);
        }

        return entityObj;
    }


    @Override
    public Map<K, ED> read(final Collection<K> inIds) {
        // Sanity checks
        if (Objects.isNull(inIds) || inIds.isEmpty()) {
            return new HashMap<>();
        }

        // Read :: IDs
        final Set<K> ids = inIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        final List<E> entityObjs = this.dao.read(this.getEntityClass(), ids);
        if (Objects.isNull(entityObjs) || entityObjs.isEmpty()) {
            return new HashMap<>();
        }

        // asMap
        final Map<K, ED> entityDTOmap = entityObjs.stream() //
                .filter(Objects::nonNull) //
                .collect(Collectors.toMap(E::getId, this::toDTO));

        return entityDTOmap;
    }


    @Override
    public int count() {
        return this.dao.count(this.getEntityClass());
    }

    @Override
    public List<ED> read(int pageNo, int pageSize) {
        // Read
        final List<E> entityObjs = this.dao.read(this.getEntityClass(), pageNo, pageSize);

        // toDTOs
        final List<ED> entityDTOs = this.toDTOs(entityObjs);
        return entityDTOs;
    }


    @Override
    public int count(final SearchQuery<? extends E> searchQuery) {
        // Sanity checks
        if (Objects.isNull(searchQuery)) {
            throw new IllegalArgumentException("#count :: SearchQuery object is NULL");
        }

        // SearchQuery as Criteria
        final Criteria criteria = this.asCriteria(searchQuery);

        // Count
        final int count = this.dao.count(this.getEntityClass(), criteria);
        LOGGER.debug("# of objects found for search :: {} - {} : {}", this.getEntityClass(), criteria, count);

        return count;
    }

    @Override
    public List<ED> search(final SearchQuery<? extends E> searchQuery, final int pageNo, final int pageSize) {
        // Sanity checks
        if (Objects.isNull(searchQuery)) {
            throw new IllegalArgumentException("#search :: SearchQuery object is NULL");
        }

        // SearchQuery as Criteria
        final Criteria criteria = this.asCriteria(searchQuery);

        // Find
        final List<E> entityObjs = this.dao.find(this.getEntityClass(), criteria, pageNo, pageSize);
        return this.toDTOs(entityObjs);
    }
}
