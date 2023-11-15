package com.mandark.jira.spi.web;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Bean to hold Pagination information any object collection.
 */
public final class Pagination {

    private final int totalCount;

    private final int pageNo;
    private final int pageSize;

    private final int totalPages;

    private final int pageFirst;
    private final int pageLast;


    // Constructor

    private Pagination(int totalCount, int pageNo, int pageSize) {
        super();

        // initialize
        this.totalCount = totalCount;

        this.pageNo = pageNo;
        this.pageSize = pageSize;

        this.totalPages = pageSize < 1 ? 0 : (totalCount - 1) / pageSize + 1;

        // prevPageIndex
        int prevPageLast = (pageNo - 1) * pageSize;
        int remaining = totalCount - prevPageLast;

        this.pageFirst = prevPageLast + (remaining > 0 ? 1 : 0);
        this.pageLast = prevPageLast + (remaining < pageSize ? remaining : pageSize);
    }


    // Getters and Setters

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    @JsonIgnore
    public int getTotalPages() {
        return totalPages;
    }

    @JsonIgnore
    public int getPageFirst() {
        return pageFirst;
    }

    @JsonIgnore
    public int getPageLast() {
        return pageLast;
    }


    // Object Methods

    @Override
    public String toString() {
        return "Pagination [totalCount=" + totalCount + ", pageNo=" + pageNo + ", pageSize=" + pageSize + "]";
    }


    // Factory Methods
    // ------------------------------------------------------------------------

    public static Pagination with(int totalCount, int pageNo, int pageSize) {
        return new Pagination(totalCount, pageNo, pageSize);
    }

    public static Pagination singlePage(int count) {
        return new Pagination(count, 1, count);
    }

}
