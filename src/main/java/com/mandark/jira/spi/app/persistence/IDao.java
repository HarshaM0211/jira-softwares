package com.mandark.jira.spi.app.persistence;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.query.OrderBy;


/**
 * Data Access Object for all the Entities implementing {@link IEntity}.
 * 
 * <p>
 * This Data Access Object(DAO) provides the methods like persist, update, find/search .. to manipulate these objects.
 * </p>
 * 
 * <p>
 * Be vary of transaction management while using this object.
 * </p>
 * 
 * @param E extends {@link IEntity}
 * @param K extends {@link Serializable}
 * 
 * @see GenericAuditDao
 * @see IEntity
 */
public interface IDao<K> {

    // Save

    /**
     * Saves the Entity.
     * 
     * @param inEntity The entity to be saved.
     */
    K save(IEntity<K> inEntity);

    /**
     * Saves the Entities Collection.
     * 
     * @param inEntities The entity collection to be saved.
     */
    List<K> save(Collection<? extends IEntity<K>> inEntities);


    // Update

    /**
     * Update/Merge with the existing Entity.
     * 
     * @param id Unique Identifier of the Entity to be updated
     * @param inEntity The entity to be merged/updated.
     * 
     * @return The updated Entity.
     */
    void update(K id, IEntity<K> inEntity);

    /**
     * Update/Merge with the existing Entities.
     * 
     * @param inEntities The entities collection to be merged/updated.
     * 
     * @return The updated Entity List.
     */
    void update(Collection<? extends IEntity<K>> inEntities);



    // TODO :: add Delete (soft)


    // Purge

    /**
     * Purges the entity (hard delete).
     * 
     * @param entityCls the entity type
     * @param id Unique Identifier of the Entity
     */
    void purge(Class<? extends IEntity<K>> entityCls, K id);

    /**
     * Purges the entities (hard delete).
     * 
     * @param entityCls the entity type
     * @param idList collection of entity Unique Identifiers
     */
    void purge(Class<? extends IEntity<K>> entityCls, Collection<K> idList);


    // Read
    // ------------------------------------------------------------------------

    /**
     * Fetches the entity with the passed identifier and type.
     * 
     * @param entityCls the entity type
     * @param id entity identifier
     * @param errorIfNotFound setting it to <code>true</code> will throw an exception if no entity is found.
     * 
     * @return The entity object with the passed identifier
     */
    <E extends IEntity<K>> E read(Class<E> entityCls, K id, boolean errorIfNotFound);

    /**
     * Fetches the entities with the passed identifiers and type.
     * 
     * @param entityCls the entity type
     * @param inIdList entity Unique identifiers
     * 
     * @return {@link List} of entity objects
     */
    <E extends IEntity<K>> List<E> read(Class<E> entityCls, Collection<K> inIdList);



    /**
     * Gets the total count of a given entity entries.
     * 
     * @param entityCls the entity type
     * 
     * @return total entity count.
     */
    <E extends IEntity<K>> int count(Class<E> entityCls);

    /**
     * Fetches a list entities. The results of this method are paginated
     * 
     * @param entityCls the entity type
     * @param pageNo pagination - page number
     * @param pageSize pagination - page size
     * 
     * @return {@link List} of entity objects
     */
    <E extends IEntity<K>> List<E> read(Class<E> entityCls, int pageNo, int pageSize);


    /**
     * Fetches a list entities with the passed deleted state and type, ordered by the give parameter. The results of
     * this method are paginated
     * 
     * @param entityCls the entity type
     * @param orderby parameter name to order the objects
     * @param pageNo number of the page
     * @param pageSize Size of the page
     * 
     * @return A list of entity objects
     */
    <E extends IEntity<K>> List<E> read(Class<E> entityCls, OrderBy orderby, int pageNo, int pageSize);



    // Find

    /**
     * Gets the total count of a given entity entries with the passed entity select criteria.
     * 
     * @param entityCls the entity type
     * @param criteria entity select criteria
     * 
     * @return {@link List} of entity objects matching the {@link Criteria}
     */
    <E extends IEntity<K>> int count(Class<E> entityCls, Criteria criteria);

    /**
     * Fetches/selects a List of entities matching with the given criteria paginated.
     * 
     * @param entityCls the entity type
     * @param criteria entity select criteria
     * @param pageNum pagination - page number
     * @param pageSize pagination - page size
     * 
     * @return {@link List} of entity objects matching the {@link Criteria}
     */
    <E extends IEntity<K>> List<E> find(Class<E> entityCls, Criteria criteria, int pageNo, int pageSize);

    /**
     * Fetches/selects a List of entities matching in the order with the given criteria paginated
     * 
     * @param entityCls the entity type
     * @param criteria entity select criteria
     * @param orderBy order by property
     * @param pageNo pagination - page number
     * @param pageSize pagination - page size
     * 
     * @return {@link List} of entity objects matching the {@link Criteria}
     */
    <E extends IEntity<K>> List<E> find(Class<E> entityCls, Criteria criteria, OrderBy orderBy, int pageNo,
            int pageSize);



    // Other Utilities

    /**
     * Empties the entity table/collection
     * 
     * @param entityCls the entity type
     */
    void truncate(Class<? extends IEntity<K>> entityCls);



    // Default Methods
    // ------------------------------------------------------------------------

    default <E extends IEntity<K>> E findOne(final Class<E> entityCls, final Criteria criteria) {
        return this.findOne(entityCls, criteria, null);
    }

    default <E extends IEntity<K>> E findOne(final Class<E> entityCls, final Criteria criteria, final OrderBy orderBy) {
        // Read entities with pageNo = 1 and pageSize = 1
        final List<E> entities = this.find(entityCls, criteria, orderBy, 1, 1);
        if (Objects.isNull(entities) || entities.isEmpty()) {
            return null;
        }

        return entities.get(0);
    }


    /**
     * Fetches the physical table column names of the current entity.
     * 
     * @param entityCls the entity type
     * 
     * @return a List of strings containing the entity table column names
     */
    default List<String> getEntityProperties(final Class<IEntity<K>> entityCls) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#getEntityProperties :: in Entity Class is NULL");
        }

        // Field Names
        final List<String> fieldNames = new ArrayList<>();

        // Declared Fields
        final Field[] fields = entityCls.getDeclaredFields();
        for (final Field field : fields) {
            if (Collection.class.isAssignableFrom(field.getType())) {
                continue; // Skip Collection fields
            }

            fieldNames.add(field.getName());
        }

        // Inherited Fields
        final Field[] inheritedFields = entityCls.getSuperclass().getDeclaredFields();
        for (final Field inhField : inheritedFields) {
            if (Collection.class.isAssignableFrom(inhField.getType())) {
                continue; // Skip Collection fields
            }

            fieldNames.add(inhField.getName());
        }

        return fieldNames;
    }


}
