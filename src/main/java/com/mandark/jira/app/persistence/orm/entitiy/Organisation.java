package com.mandark.jira.app.persistence.orm.entitiy;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;



@Entity
@Table(name = "organisations")
public class Organisation extends JpaAuditEntity {

    private String name;

    private String description;

    private List<Project> projects;

    private List<Sprint> sprints;

    private List<Team> teams;

    // Constructors
    // ------------------------------------------------------------------------

    // Default
    public Organisation() {
        super();
    }

    public Organisation(String name, String description, List<Project> projects, List<Sprint> sprints,
            List<Team> teams) {
        super();
        this.name = name;
        this.description = description;
        this.projects = projects;
        this.sprints = sprints;
        this.teams = teams;
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

    @OneToMany(mappedBy = "organisation")
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @OneToMany(mappedBy = "organisation")
    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    @OneToMany(mappedBy = "organisation")
    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Organisations [name=" + name + ", description=" + description + ", project_ids=" + projects
                + ", sprint_ids=" + sprints + ", team_ids=" + teams + "]";
    }



}
