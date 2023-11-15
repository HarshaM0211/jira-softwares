package com.mandark.jira.spi.app.query;

import java.util.Collection;


/**
 * A {@link CompoundCriteria} criteria operated on Logical <b>AND</b>.
 */
public class AndCriteria extends CompoundCriteria {

    // Constructor

    AndCriteria(Collection<Criteria> criterias) {
        super(criterias);
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "AndCriteria [criteriaList=" + criteriaList + "]";
    }


}
