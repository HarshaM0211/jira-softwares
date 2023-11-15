package com.mandark.jira.app.persistence.orm;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mandark.jira.spi.app.Validatable;
import com.mandark.jira.spi.app.persistence.IEntity;


/**
 * Base definition for persistable meta objects/entities.
 *
 * @see JpaAuditEntity
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public abstract class JpaEntity implements IEntity<Integer>, Validatable {

    // Field Labels
    public static final String FIELD_ID = "id";


    // Properties
    private Integer id;


    // Getters and Setters
    // ------------------------------------------------------------------------

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", unique = true)
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() throws RuntimeException {
        // default checks
    }


}
