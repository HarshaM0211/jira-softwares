package com.mandark.jira.spi.app.query;


/**
 * A {@link PropertyCriteria} criteria to ensure the Data Property has NULL or BLANK value.
 */
public class NullCriteria extends PropertyCriteria<Void> {

    // Constructor

    NullCriteria(String property) {
        super(property, null);
    }


    // Object Methods

    @Override
    public String toString() {
        return "NullCriteria [property=" + property + "]";
    }


}
