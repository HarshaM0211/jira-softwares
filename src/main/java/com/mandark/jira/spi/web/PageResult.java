package com.mandark.jira.spi.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * Paginated response.
 */
public final class PageResult {

    private final Pagination pagination;
    private final List<?> items;


    // Constructor
    // ------------------------------------------------------------------------

    private PageResult(Pagination pagination, Collection<?> items) {
        super();

        // init
        this.pagination = pagination;
        this.items = Objects.isNull(items) ? new ArrayList<>() : new ArrayList<>(items);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public Pagination getPagination() {
        return pagination;
    }

    public List<?> getItems() {
        return Collections.unmodifiableList(items);
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "PageResult [pagination=" + pagination + ", items=" + items.size() + "]";
    }


    // Factory
    // ------------------------------------------------------------------------

    public static PageResult with(Pagination pagination, Collection<?> items) {
        return new PageResult(pagination, items);
    }

    public static PageResult singlePage(Collection<?> items) {
        final int itemsCount = Objects.isNull(items) ? 0 : items.size();
        return new PageResult(Pagination.singlePage(itemsCount), items);
    }

    public static PageResult empty() {
        return new PageResult(Pagination.singlePage(0), new ArrayList<>());
    }


}
