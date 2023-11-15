package com.mandark.jira.spi.app.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Abstract definition of a {@link Criteria} that is a logical combination of two or more {@link Criteria}s
 * 
 * @see PropertyCriteria
 */
public abstract class CompoundCriteria implements Criteria {

    protected final List<Criteria> criteriaList;


    // Constructor

    CompoundCriteria(Collection<Criteria> criterias) {
        super();

        assert criterias != null && !criterias.isEmpty() : "CompoundCriteria :: Criteria list is EMPTY";

        // Clean the inputs and add
        this.criteriaList = criterias == null || criterias.isEmpty() //
                ? new ArrayList<>() //
                : criterias.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    // Getters and Setters

    /**
     * The collection of {@link Criteria} compouding
     * 
     * @return
     */
    public List<Criteria> getCriteriaList() {
        return Collections.unmodifiableList(criteriaList);
    }


}
