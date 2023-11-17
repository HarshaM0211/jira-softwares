package com.mandark.jira.app.dto;

import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.spi.app.EntityDTO;



// TODO
public class OrganisationDTO extends EntityDTO<Organisation> {

    private final String name;

    private final String description;


    // Constructors
    // ------------------------------------------------------------------------

    // Default
    public OrganisationDTO(Organisation e) {
        super(e);
        this.name = e.getName();
        this.description = e.getDescription();

    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "OrganisationsDTO [name=" + name + ", description=" + description + "]";
    }

}
