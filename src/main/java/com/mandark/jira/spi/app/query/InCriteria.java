package com.mandark.jira.spi.app.query;

import java.util.Collection;
import java.util.Collections;


/**
 * A {@link PropertyCriteria} criteria to match the Data Property value with any one of the passed values.
 */
public class InCriteria extends PropertyCriteria<Collection<?>> {


    // Constructor

    InCriteria(String property, Collection<?> values) {
        super(property, values);

        assert values != null && !values.isEmpty() : "InCriteria :: Values Collection is EMPTY";
    }


    // Getters and Setters

    @Override
    public Collection<?> getValue() {
        return Collections.unmodifiableCollection(value);
    }

    public Collection<?> getValues() {
        return this.getValue();
    }


    // Object Methods

    @Override
    public String toString() {
        return "InCriteria [property=" + property + ", values=" + value + "]";
    }


}
