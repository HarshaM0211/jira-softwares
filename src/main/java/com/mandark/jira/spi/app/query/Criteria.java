package com.mandark.jira.spi.app.query;

import java.util.Arrays;
import java.util.Collection;



/**
 * Marker Interface to define criteria (in other words, the filtering clause used in any query).
 */
public interface Criteria {


    // Criteria Factory Implementation
    // ------------------------------------------------------------------------


    // Criteria :: Property

    public static EqualsCriteria equal(String property, Object value) {
        return new EqualsCriteria(property, value, false);
    }

    public static EqualsCriteria equalIgnoreCase(String property, Object value) {
        return new EqualsCriteria(property, value, true);
    }

    public static InCriteria in(String property, Collection<?> values) {
        return new InCriteria(property, values);
    }

    public static LikeCriteria like(String property, String value) {
        return new LikeCriteria(property, value);
    }

    public static MinCriteria min(String property, Object value) {
        return new MinCriteria(property, value);
    }

    public static MaxCriteria max(String property, Object value) {
        return new MaxCriteria(property, value);
    }

    public static NotNullCriteria notNull(String property) {
        return new NotNullCriteria(property);
    }

    public static NullCriteria isNull(String property) {
        return new NullCriteria(property);
    }


    // Criteria :: Compound

    public static Criteria and(Criteria... criteriaList) {
        // Sanity checks
        if (criteriaList == null || criteriaList.length == 0) {
            throw new IllegalArgumentException("Criteria#and :: input Criteria array is EMPTY");
        }

        return new AndCriteria(Arrays.asList(criteriaList));
    }

    public static Criteria and(Collection<Criteria> criteriaList) {
        return new AndCriteria(criteriaList);
    }

    public static Criteria or(Criteria... criteriaList) {
        // Sanity checks
        if (criteriaList == null || criteriaList.length == 0) {
            throw new IllegalArgumentException("Criteria#or :: input Criteria array is EMPTY");
        }

        return new OrCriteria(Arrays.asList(criteriaList));
    }

    public static Criteria or(Collection<Criteria> criteriaList) {
        return new OrCriteria(criteriaList);
    }


}
