package com.mandark.jira.spi.app.query;


/**
 * A {@link PropertyCriteria} criteria to match the Data Property value to be at most (max.) the passed value.
 */
public class MaxCriteria extends PropertyCriteria<Object> {


    // Constructor

    MaxCriteria(String property, Object value) {
        super(property, value);

        assert value != null : "MaxCriteria :: value is NULL";
    }


    // Object Methods

    @Override
    public String toString() {
        return "MaxCriteria [property=" + property + ", value=" + value + "]";
    }


}
