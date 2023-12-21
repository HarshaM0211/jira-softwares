package com.mandark.jira.spi.app.query;

/**
 * Class to provide a way to sort the entities
 */
public final class OrderBy {

    private final String property;
    private final boolean isAsc;


    // Constructors

    public OrderBy(final String property, final boolean isAsc) {
        super();

        // init
        this.property = property;
        this.isAsc = isAsc;
    }


    // Getters and Setters

    public String getProperty() {
        return property;
    }

    public boolean isAsc() {
        return isAsc;
    }


    // Object Methods

    @Override
    public String toString() {
        return "OrderBy [property=" + property + ", isAsc=" + isAsc + "]";
    }
}
