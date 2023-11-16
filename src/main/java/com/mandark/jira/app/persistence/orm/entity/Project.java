package com.mandark.jira.app.persistence.orm.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;



@Entity
@Table(name = "projects")
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

    // public Project(Organisation organisation, String project_key, String project_name, String project_description,
    // List<Sprint> sprint_ids, List<User> org_members, List<Issue> issues) {
    // super();
    // this.organisation = organisation;
    // this.projectKey = project_key;
    // this.name = project_name;
    // this.description = project_description;
    // this.sprints = sprint_ids;
    // this.users = org_members;
    // this.issues = issues;
    // }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "org_id")
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    @Column(name = "project_key")
    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String project_key) {
        this.projectKey = project_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String project_name) {
        this.name = project_name;
    }

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
        return "Projects [organisation=" + organisation + ", project_key=" + projectKey + ", project_name=" + name
                + ", project_description=" + description + ", sprints=" + sprints + ", org_members=" + users
                + ", issues=" + issues + "]";
    }



}
