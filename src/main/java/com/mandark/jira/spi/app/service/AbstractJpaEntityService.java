package com.mandark.jira.spi.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.spi.app.EntityBean;
import com.mandark.jira.spi.app.EntityDTO;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.persistence.IEntity;


/**
 * Abstract implementation of {@link EntityService} for JPA (ORM) Entities
 * 
 * @param <E> Type Parameter for the Entity
 * @param <EB> Bean Type of the Entity
 * @param <ED> DTO Type of the Entity
 */
public abstract class AbstractJpaEntityService<E extends IEntity<Integer>, EB extends EntityBean<E>, ED extends EntityDTO<E>> //
        extends AbstractEntityService<Integer, E, EB, ED> //
        implements EntityService<Integer, E, ED> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJpaEntityService.class);


    // Constructor
    // ------------------------------------------------------------------------

    public AbstractJpaEntityService(IDao<Integer> dao) {
        super(dao);
    }


    // Protected Methods
    // ------------------------------------------------------------------------

    // Create

    @Override
    @Transactional
    protected Integer save(final EB entityBean) {
        // Sanity checks
        if (Objects.isNull(entityBean)) {
            throw new IllegalArgumentException("#save :: Entity Bean Object is NULL");
        }

        // toEntity
        final E entity = this.createFromBean(entityBean);

        final Integer id = this.dao.save(entity);
        LOGGER.debug("Successfully saved entity object : {}", entity);

        return id;
    }

    @Override
    @Transactional
    protected List<Integer> save(final Collection<EB> entityBeans) {
        // Sanity checks
        if (Objects.isNull(entityBeans) || entityBeans.isEmpty()) {
            return new ArrayList<>();
        }

        // Entities
        final List<E> entityList = new ArrayList<>();

        // Iterate and save
        for (final EB eb : entityBeans) {
            if (Objects.isNull(eb)) {
                continue;
            }

            // toEntity
            final E entity = this.createFromBean(eb);
            entityList.add(entity);
        }

        final List<Integer> idList = this.dao.save(entityList);
        LOGGER.debug("Successfully completed creating entity objects : {}", entityBeans.size());

        return idList;
    }


    // Update

    @Transactional
    protected void update(final Integer id, final EB entityBean) {
        // Sanity checks
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("#update :: Entity ID is NULL");
        }

        if (Objects.isNull(entityBean)) {
            throw new IllegalArgumentException("#update :: Entity Bean Object is NULL");
        }

        // existing entity
        final E exEntity = dao.read(getEntityClass(), id, false);

        // copy toEntity
        final E entity = this.copyFromBean(exEntity, entityBean);

        this.dao.update(id, entity);
        LOGGER.debug("Successfully updated entity object : {} - {}", id, entity);
    }


    // Purge

    @Transactional
    protected void purge(final Integer id) {
        // Sanity checks
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("#purge :: Entity ID is NULL");
        }

        // Purge
        this.dao.purge(this.getEntityClass(), id);
        LOGGER.debug("Successfully purged entity object[id] : {}", id);
    }


    @Transactional
    protected void purge(Collection<Integer> idsList) {
        // Sanity checks
        if (Objects.isNull(idsList) || idsList.isEmpty()) {
            return;
        }

        // Purge
        this.dao.purge(this.getEntityClass(), idsList);
        LOGGER.debug("Successfully completed purging entity objects : {}", idsList.size());
    }


    // Other

}