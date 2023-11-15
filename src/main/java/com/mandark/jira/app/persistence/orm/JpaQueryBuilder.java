package com.mandark.jira.app.persistence.orm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.spi.app.persistence.IEntity;
import com.mandark.jira.spi.app.persistence.QueryBuilder;
import com.mandark.jira.spi.app.query.AndCriteria;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.query.EqualsCriteria;
import com.mandark.jira.spi.app.query.InCriteria;
import com.mandark.jira.spi.app.query.LikeCriteria;
import com.mandark.jira.spi.app.query.MaxCriteria;
import com.mandark.jira.spi.app.query.MinCriteria;
import com.mandark.jira.spi.app.query.NotNullCriteria;
import com.mandark.jira.spi.app.query.NullCriteria;
import com.mandark.jira.spi.app.query.OrCriteria;
import com.mandark.jira.spi.app.query.PropertyCriteria;


/**
 * This is the {@link QueryBuilder} implementation for JPA entity objects..
 */
class JpaQueryBuilder implements QueryBuilder<Query> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaQueryBuilder.class);

    private static final String INSTANCE_ALIAS = "ins";


    private final EntityManager entityManager;


    // Constructor

    JpaQueryBuilder(final EntityManager entityManager) {
        super();

        // init
        this.entityManager = entityManager;
    }


    // Methods
    // ------------------------------------------------------------------------

    @Override
    public <E extends IEntity<?>> Query toQuery(final Class<E> entityCls) {
        return this.toQuery(entityCls, null);
    }

    @Override
    public <E extends IEntity<?>> Query toQuery(final Class<E> entityCls, final Criteria inCriteria) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#toQuery :: IEntity class in NULL");
        }

        // Construct Query
        final JpaQuery jpaQuery = new JpaQuery(entityCls, inCriteria, false);
        final String jpaQueryStr = jpaQuery.getQueryString();
        final Hashtable<String, Object> qryParamValues = jpaQuery.getQueryParamValues();
        LOGGER.debug("JPA Query :: [{}] : {} - {}", inCriteria, jpaQueryStr, qryParamValues);

        // Create Entity Query
        final Query query = entityManager.createQuery(jpaQueryStr);
        this.applyQueryParamValues(query, qryParamValues);

        return query;
    }


    @Override
    public <E extends IEntity<?>> Query toCountQuery(Class<E> entityCls) {
        return this.toCountQuery(entityCls, null);
    }

    @Override
    public <E extends IEntity<?>> Query toCountQuery(Class<E> entityCls, Criteria inCriteria) {
        // Sanity checks
        if (Objects.isNull(entityCls)) {
            throw new IllegalArgumentException("#toCountQuery :: IEntity class in NULL");
        }

        // Construct Query
        final JpaQuery jpaQuery = new JpaQuery(entityCls, inCriteria, true);
        final String jpaQueryStr = jpaQuery.getQueryString();
        final Hashtable<String, Object> qryParamValues = jpaQuery.getQueryParamValues();
        LOGGER.debug("JPA Count Query :: [{}] : {} - {}", inCriteria, jpaQueryStr, qryParamValues);

        // Create Entity Query
        final Query query = entityManager.createQuery(jpaQueryStr);
        this.applyQueryParamValues(query, qryParamValues);

        return query;
    }


    // Private Utilities
    // ------------------------------------------------------------------------

    /**
     * Prepares the named query with the provided criteria.
     * 
     * @param jpaQry JPA Query(Named Query) to be executed
     * @param qryParamValues entity select criteria
     */
    private void applyQueryParamValues(final Query jpaQry, final Hashtable<String, Object> qryParamValues) {
        // Sanity checks
        if (Objects.isNull(qryParamValues) || qryParamValues.isEmpty()) {
            return;
        }

        // Iterate and apply
        final Enumeration<String> keys = qryParamValues.keys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            final Object value = qryParamValues.get(key);

            jpaQry.setParameter(key, value);
        }

    }


    // Property Parameter

    private static final BiFunction<String, Integer, String> JPQL_PARAM = (propName, index) -> {
        final String crTxt = String.format("%s_param_%d", propName, index);
        return crTxt;
    };


    // Criteria

    private static final BiFunction<String, String, String> JPQL_EQUAL = (propName, propParam) -> {
        final String crTxt = String.format("%s.%s = :%s", INSTANCE_ALIAS, propName, propParam);
        return crTxt;
    };

    private static final BiFunction<String, String, String> JPQL_IN = (propName, propParam) -> {
        final String crTxt = String.format("%s.%s IN (:%s)", INSTANCE_ALIAS, propName, propParam);
        return crTxt;
    };

    private static final BiFunction<String, String, String> JPQL_LIKE = (propName, propParam) -> {
        final String crTxt = String.format("%s.%s LIKE CONCAT('%%',:%s,'%%')", INSTANCE_ALIAS, propName, propParam);
        return crTxt;
    };

    private static final BiFunction<String, String, String> JPQL_MIN = (propName, propParam) -> {
        final String crTxt = String.format("%s.%s > :%s", INSTANCE_ALIAS, propName, propParam);
        return crTxt;
    };

    private static final BiFunction<String, String, String> JPQL_MAX = (propName, propParam) -> {
        final String crTxt = String.format("%s.%s < :%s", INSTANCE_ALIAS, propName, propParam);
        return crTxt;
    };

    private static final Function<String, String> JPQL_NOT_NULL = (propName) -> {
        final String crTxt = String.format("%s.%s IS NOT NULL", INSTANCE_ALIAS, propName);
        return crTxt;
    };

    private static final Function<String, String> JPQL_IS_NULL = (propName) -> {
        final String crTxt = String.format("%s.%s IS NULL", INSTANCE_ALIAS, propName);
        return crTxt;
    };



    // JpaQuery Class definition
    // ------------------------------------------------------------------------


    final static class JpaQuery {

        private final String queryString;
        private final Hashtable<String, Object> queryParamValues;


        // Constructor
        // --------------------------------------------------------------------

        JpaQuery(final Class<?> entityCls, final Criteria inCriteria, boolean isCountQuery) {
            super();

            // Entity Name
            final String entityName = entityCls.getSimpleName();

            final StringBuilder querySB = new StringBuilder();
            querySB.append("SELECT ");
            if (isCountQuery) {
                querySB.append("COUNT(").append(INSTANCE_ALIAS).append(")");
            } else {
                querySB.append(INSTANCE_ALIAS);
            }
            querySB.append(" FROM ");
            querySB.append(entityName).append(" ");
            querySB.append(INSTANCE_ALIAS);

            // WHERE clause
            final Hashtable<String, Object> jpaQueryParamValues = new Hashtable<>();
            if (Objects.nonNull(inCriteria)) {
                querySB.append(" WHERE ");
                querySB.append(this.asCriteriaTxt(inCriteria, jpaQueryParamValues));
            }

            // TODO Order By

            final String jpaQueryStr = querySB.toString();

            // init
            this.queryString = jpaQueryStr;
            this.queryParamValues = jpaQueryParamValues;
        }


        // Methods
        // --------------------------------------------------------------------

        // Criteria :: Compound

        private String asCriteriaTxt(final AndCriteria andCriteria, final Map<String, Object> paramValues) {
            // Criteria Strings
            final List<String> criteriaStrs = new ArrayList<>();
            for (final Criteria cr : andCriteria.getCriteriaList()) {
                final String crStr = this.asCriteriaTxt(cr, paramValues);
                criteriaStrs.add(crStr);
            }

            return "(" + String.join(" AND ", criteriaStrs) + ")";
        }

        private String asCriteriaTxt(final OrCriteria orCriteria, final Map<String, Object> paramValues) {
            // Criteria Strings
            final List<String> criteriaStrs = new ArrayList<>();
            for (final Criteria cr : orCriteria.getCriteriaList()) {
                final String crStr = this.asCriteriaTxt(cr, paramValues);
                criteriaStrs.add(crStr);
            }

            return "(" + String.join(" OR ", criteriaStrs) + ")";
        }


        // Criteria :: Generic

        private String asCriteriaTxt(final Criteria criteria, final Map<String, Object> paramValues) {
            // Handle Criteria Group
            if (criteria instanceof AndCriteria) {
                return this.asCriteriaTxt((AndCriteria) criteria, paramValues);
            } else if (criteria instanceof OrCriteria) {
                return this.asCriteriaTxt((OrCriteria) criteria, paramValues);
            }

            // Update the index
            final int crIdx = paramValues.size() + 1;

            // Property Criteria
            final PropertyCriteria<?> propCriteria = (PropertyCriteria<?>) criteria;
            final String propName = propCriteria.getProperty();
            final String propParam = JPQL_PARAM.apply(propName, crIdx);

            final Object propValue = propCriteria.getValue();
            if (Objects.nonNull(propValue)) {
                paramValues.put(propParam, propValue);
            }

            // Criteria
            if (criteria instanceof EqualsCriteria) {
                return JPQL_EQUAL.apply(propName, propParam);

            } else if (criteria instanceof InCriteria) {
                return JPQL_IN.apply(propName, propParam);

            } else if (criteria instanceof LikeCriteria) {
                return JPQL_LIKE.apply(propName, propParam);

            } else if (criteria instanceof MinCriteria) {
                return JPQL_MIN.apply(propName, propParam);

            } else if (criteria instanceof MaxCriteria) {
                return JPQL_MAX.apply(propName, propParam);

            } else if (criteria instanceof NotNullCriteria) {
                return JPQL_NOT_NULL.apply(propName);

            } else if (criteria instanceof NullCriteria) {
                return JPQL_IS_NULL.apply(propName);

            }

            final String cName = criteria.getClass().getSimpleName();
            final String errMsg = String.format("JpaQueryBuilder#from :: unknown criteria type : %s", cName);
            LOGGER.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }


        // Getters and Setters
        // --------------------------------------------------------------------

        public String getQueryString() {
            return queryString;
        }

        public Hashtable<String, Object> getQueryParamValues() {
            return queryParamValues;
        }


        // Object Methods
        // --------------------------------------------------------------------

        @Override
        public String toString() {
            return "JpaQuery [queryStr=" + queryString + ", paramValues=" + queryParamValues + "]";
        }

    }



    // if (orderby == null) {
    // sQuery += " ORDER BY instance.createdOn DESC";
    // } else {
    // sQuery += " ORDER BY instance." + orderby;
    // }


}
