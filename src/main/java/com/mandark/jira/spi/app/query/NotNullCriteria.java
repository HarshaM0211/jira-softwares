package com.mandark.jira.spi.app.query;


/**
 * A {@link PropertyCriteria} criteria to ensure the Data Property has a non-NULL value.
 */
public class NotNullCriteria extends PropertyCriteria<Void> {

    // Constructor

    NotNullCriteria(String property) {
        super(property, null);
    }


    // Object Methods

    @Override
    public String toString() {
        return "NotNullCriteria [property=" + property + "]";
    }


}
