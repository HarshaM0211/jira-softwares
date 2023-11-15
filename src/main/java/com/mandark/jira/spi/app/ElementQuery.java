package com.mandark.jira.spi.app;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * {@link SearchQuery} definitions for Geo Element data
 */
public class ElementQuery extends SearchQuery {

    // Keys
    public static final String KEY_TYPE = "type";
    public static final String KEY_CATEGORY_ID = "cat";

    // Properties
    private final List<String> types;
    private final List<String> categoryIds;


    // Constructors
    // ------------------------------------------------------------------------

    public ElementQuery(Map<String, List<String>> criteria) {
        super(criteria);

        // init
        this.types = this.getList(KEY_TYPE);
        this.categoryIds = this.getList(KEY_CATEGORY_ID);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public List<String> getTypes() {
        return Collections.unmodifiableList(types);
    }

    public List<String> getCategoryIds() {
        return Collections.unmodifiableList(categoryIds);
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ElementQuery [query=" + query + ", criteria=" + criteria + "]";
    }


}
