package com.mandark.jira.app.persistence.orm.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;



@Entity
@Table(name = "organisations", indexes = {@Index(columnList = "name", name = "name")})
public class Organisation extends JpaAuditEntity {

    // Field Lables
    // ------------------------------------------------------------------------

    public static final String PROP_NAME = "name";

    // Fields
    // ------------------------------------------------------------------------

    private String name;

    private String description;

    private List<Project> projects;

    private List<Team> teams;

    private List<User> users;

    // Constructors
    // ------------------------------------------------------------------------

    // Default
    public Organisation() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(name)) {
            throw new ValidationException("#validate :: name is BLANK");
        }

    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
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
    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @OneToMany(mappedBy = "organisation")
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // Object Methods
    // ------------------------------------------------------------------------


    @Override
    public String toString() {
        return "Organisations [name=" + name + ", description=" + description + "]";
    }
}
