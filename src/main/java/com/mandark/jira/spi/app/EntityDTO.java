package com.mandark.jira.spi.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mandark.jira.spi.app.persistence.IAuditEntity;
import com.mandark.jira.spi.app.persistence.IEntity;


/**
 * Abstract implementation for entity DTO definitions.
 * 
 * @param <E> corresponding {@link IEntity} of the DTO object
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class EntityDTO<E extends IEntity<?>> extends AbstractDTO<E> {

    protected final String id;
    protected final Integer version;


    // Constructors
    // ------------------------------------------------------------------------

    protected EntityDTO(E e) {
        this(e, true);
    }

    protected EntityDTO(E e, boolean withAuditInfo) {
        super(e);

        // init
        this.id = String.valueOf(e.getId());

        // CreatedOn and UpdatedOn
        if (e instanceof IAuditEntity && withAuditInfo) {
            final IAuditEntity<?> be = (IAuditEntity<?>) e;
            this.version = be.getVersion();
        } else {
            this.version = null;
        }
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        final String entityCls = this.getClass().getSimpleName();
        return "EntityDTO [entity=" + entityCls + ", id=" + id + ", version=" + version + "]";
    }

}
