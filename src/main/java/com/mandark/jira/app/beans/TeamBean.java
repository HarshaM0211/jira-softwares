package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.spi.app.EntityBean;


public class TeamBean implements EntityBean<Team> {

    // Fields
    // ------------------------------------------------------------------------
    private String name;

    private Organisation organisation;

    // Constructors
    // ------------------------------------------------------------------------

    public TeamBean() {
        super();
    }

    public TeamBean(String name, Organisation organisation) {
        super();
        this.name = name;
        this.organisation = organisation;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "TeamBean [name=" + name + ", organisation=" + organisation + "]";
    }
}
