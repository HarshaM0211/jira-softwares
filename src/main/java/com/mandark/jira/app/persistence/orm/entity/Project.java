package com.mandark.jira.app.persistence.orm.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;



@Entity
@Table(name = "projects", indexes = {@Index(columnList = "org_id", name = "org_id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"org_id", "name"})})
public class Project extends JpaAuditEntity {

    private Organisation organisation;

    private String projectKey;

    private String name;

    private String description;

    private List<Sprint> sprints;

    private List<User> users;

    private List<Issue> issues;

    // Constructors
    // ------------------------------------------------------------------------

    public Project() {
        super();
        // TODO Auto-generated constructor stub
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(organisation)) {
            throw new ValidationException("#validate :: organisation is BLANK");
        }

        if (Objects.isNull(projectKey)) {
            throw new ValidationException("#validate :: projectKey is BLANK");
        }

        if (Objects.isNull(name)) {
            throw new ValidationException("#validate :: name is BLANK");
        }

    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    @Column(name = "project_key", nullable = false, unique = true)
    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String project_key) {
        this.projectKey = project_key;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String project_name) {
        this.name = project_name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String project_description) {
        this.description = project_description;
    }

    @OneToMany(mappedBy = "project")
    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    @ManyToMany
    @JoinColumn(name = "user_id")
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> org_members) {
        this.users = org_members;
    }

    @OneToMany(mappedBy = "project")
    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Projects [organisation=" + organisation.getId() + ", project_key=" + projectKey + ", project_name="
                + name + ", project_description=" + "]";
    }



}
