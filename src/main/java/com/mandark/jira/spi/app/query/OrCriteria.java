package com.mandark.jira.spi.app.query;

import java.util.Collection;


/**
 * A {@link CompoundCriteria} criteria operated on Logical <b>OR</b>.
 */
public class OrCriteria extends CompoundCriteria {

    // Constructor

    OrCriteria(Collection<Criteria> criterias) {
        super(criterias);
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "OrCriteria [criteriaList=" + criteriaList + "]";
    }


}
