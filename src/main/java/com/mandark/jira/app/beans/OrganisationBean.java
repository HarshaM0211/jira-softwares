package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entitiy.Organisation;
import com.mandark.jira.spi.app.EntityBean;

public class OrganisationBean implements EntityBean<Organisation> {

    // Fields
    // ------------------------------------------------------------------------

    private String name;

    private String description;

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
