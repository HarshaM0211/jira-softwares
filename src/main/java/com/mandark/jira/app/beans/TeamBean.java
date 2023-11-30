package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.spi.app.EntityBean;


public class TeamBean implements EntityBean<Team> {

    // Fields
    // ------------------------------------------------------------------------
    private String name;

    private String description;

    // Constructors
    // ------------------------------------------------------------------------

    public TeamBean() {
        super();
    }

    public TeamBean(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

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

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "TeamBean [name=" + name + ",Description = " + description + "]";
    }
}
