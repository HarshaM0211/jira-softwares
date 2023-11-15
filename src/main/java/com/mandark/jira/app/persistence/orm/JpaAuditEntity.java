package com.mandark.jira.app.persistence.orm;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mandark.jira.spi.app.Validatable;
import com.mandark.jira.spi.app.persistence.IAuditEntity;


/**
 * Base definition for persistable objects/entities. Extend this class to make an object
 * persistable.
 *
 * <p>
 * Note: {@link EntityListeners} are registered through XML (META-INF/mappings.xml)
 * </p>
 * 
 * @see JpaEntity
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public abstract class JpaAuditEntity extends JpaEntity implements IAuditEntity<Integer>, Validatable {


    @JsonIgnore
    @Version
    @Column(name = "version")
    private Integer version;


    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @JsonIgnore
    @Column(name = "updated_by")
    private String updatedBy;

    @JsonIgnore
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;


    // Getters and Setters
    // ------------------------------------------------------------------------

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }


    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }


    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    @Override
    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }


    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() throws RuntimeException {
        super.validate();
        // default checks
    }


}
