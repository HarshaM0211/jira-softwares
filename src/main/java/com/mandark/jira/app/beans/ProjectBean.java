package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.spi.app.EntityBean;


public class ProjectBean implements EntityBean<Project> {

    // Fields
    // ------------------------------------------------------------------------

    private String projectKey;

    private String name;

    private String description;

    // Constructors
    // ------------------------------------------------------------------------

    public ProjectBean() {
        super();
    }

    public ProjectBean(String projectKey, String name, String description) {
        super();
        this.projectKey = projectKey;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

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

    // Objects Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ProjectBean [projectKey=" + projectKey + ", name=" + name + ", description=" + description + "]";
    }
}
