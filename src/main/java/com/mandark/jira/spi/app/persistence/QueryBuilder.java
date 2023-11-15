package com.mandark.jira.spi.app.persistence;

import com.mandark.jira.spi.app.query.Criteria;


/**
 * Definition of a Query constructor for building Query specifc to the underlying DataStote / DataBase.
 *
 * @param <Q> Type Parameter for the output Query.
 */
public interface QueryBuilder<Q> {


    // To Query

    /**
     * Given a {@link IEntity}, constructs a Query object to find matching entries from the DataStote / DataBase.
     * 
     * Please Note that the output Query doesn't necessarily manage query parts like GroupBy, OrderBy etc.
     * 
     * @param entityCls {@link Class} of the {@link IEntity}
     * 
     * @return Query object built for the Entity.
     */
    <E extends IEntity<?>> Q toQuery(Class<E> entityCls);

    /**
     * Given a {@link IEntity} and a {@link Criteria} constructs a Query object to find matching entries from the
     * DataStote / DataBase.
     * 
     * Please Note that the output Query doesn't necessarily manage query parts like GroupBy, OrderBy etc.
     * 
     * @param entityCls {@link Class} of the {@link IEntity}
     * @param inCriteria input {@link Criteria} object
     * 
     * @return Query object built for the Entity.
     */
    <E extends IEntity<?>> Q toQuery(Class<E> entityCls, Criteria inCriteria);



    // Default Methods
    // ------------------------------------------------------------------------

    default <E extends IEntity<?>> Q toCountQuery(Class<E> entityCls) {
        return this.toQuery(entityCls);
    }

    default <E extends IEntity<?>> Q toCountQuery(Class<E> entityCls, Criteria inCriteria) {
        return this.toQuery(entityCls, inCriteria);
    }


}
