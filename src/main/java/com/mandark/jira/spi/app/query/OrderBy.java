package com.mandark.jira.spi.app.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import com.mandark.jira.spi.lang.ValidationException;


/**
 * Class to provide a way to sort the entities
 */
public final class OrderBy {

    private final List<String> properties;


    // Constructors
    // ------------------------------------------------------------------------

    public OrderBy(List<String> properties) {
        this.properties =
                (CollectionUtils.isEmpty(properties)) ? new ArrayList<String>() : new ArrayList<String>(properties);
    }

    public OrderBy(String... properties) {
        // Sanity check
        if (Objects.isNull(properties)) {
            throw new ValidationException("#OrderBy :: properties object is NULL");
        }

        this.properties = Arrays.asList(properties);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public List<String> getProperties() {
        return Collections.unmodifiableList(this.properties);
    }

}
