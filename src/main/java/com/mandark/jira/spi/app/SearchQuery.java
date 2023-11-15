package com.mandark.jira.spi.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mandark.jira.spi.util.Values;


/**
 * Query object to search/filter results.
 */
public abstract class SearchQuery {

    // Formats
    public static final String DATE_FORMAT_STR = "dd-MM-yyyy";

    // Keys
    public static final String KEY_QUERY = "q";


    // Properties
    protected final Map<String, List<String>> criteria;

    protected final String query;


    // Constructors
    // ------------------------------------------------------------------------

    protected SearchQuery(Map<String, List<String>> criteria) {
        super();

        // init
        this.criteria = Values.getMap(criteria);

        final String qryStr = this.getFirst(KEY_QUERY);
        this.query = Objects.isNull(qryStr) ? "" : qryStr;
    }


    // Methods
    // ------------------------------------------------------------------------

    protected String getFirst(final String key) {
        // Values
        final List<String> values = criteria.get(key);
        return (values != null && !values.isEmpty() ? values.get(0) : null);
    }

    protected List<String> getList(final String key) {
        // Values
        final List<String> values = criteria.get(key);
        if (Objects.isNull(values) || values.isEmpty()) {
            return new ArrayList<>();
        }

        final List<String> resultList = values.stream() //
                .filter(Objects::nonNull) //
                .filter(s -> !s.isBlank()) //
                .collect(Collectors.toList());
        return resultList;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getQuery() {
        return query;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "SearchQuery [criteria=" + criteria + "]";
    }

}
