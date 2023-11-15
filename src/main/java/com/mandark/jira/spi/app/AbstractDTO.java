package com.mandark.jira.spi.app;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mandark.jira.spi.lang.NotImplementedException;


/**
 * Abstract implementation of DTO definition for any Object.
 */
public abstract class AbstractDTO<T> {


    // Constructors
    // ------------------------------------------------------------------------

    public AbstractDTO(T t, Object... objs) {
        super();

        // Sanity checks
        if (t == null) {
            throw new IllegalArgumentException("AbstractDTO, DTO type argument should not be null");
        }
    }


    // Methods
    // ------------------------------------------------------------------------

    @JsonIgnore
    public Map<String, Object> lite() {
        String clsName = this.getClass().getSimpleName();
        String errMsg = String.format("AbstractDTO#lite() is not implemented for : %s", clsName);
        throw new NotImplementedException(errMsg);
    }


}
