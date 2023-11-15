package com.mandark.jira.spi.app.query;

import java.util.Objects;


/**
 * A {@link PropertyCriteria} criteria to match the Data Property value with containing the passed text.
 */
public class LikeCriteria extends PropertyCriteria<String> {



    // Constructor

    LikeCriteria(String property, String text) {
        super(property, text);
    }


    // Getters and Setters

    public String getText() {
        return Objects.isNull(value) ? "" : value.trim();
    }


    // Object Methods

    @Override
    public String toString() {
        return "LikeCriteria [property=" + property + ", text=" + value + "]";
    }


}
