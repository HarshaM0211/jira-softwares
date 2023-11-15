package com.mandark.jira.app.persistence.orm.entitiy;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mandark.jira.app.enums.UserRole;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;


@Entity
@Table(name = "users")
public class User extends JpaAuditEntity {

    private String userName;

    private String password;

    private String mail;

    private Organisation organisation;

    private List<Comment> comments;

    private List<Project> projects;

    private List<Team> teams;

    private UserRole role;

    // Constructors
    // ------------------------------------------------------------------------

    // Default
    public User() {
        super();
    }

    public User(String user_name, String password, String user_email, Organisation organisation, List<Comment> comments,
            List<Project> projects, List<Team> teams, UserRole role) {
        super();
        this.userName = user_name;
        this.password = password;
        this.mail = user_email;
        this.organisation = organisation;
        this.comments = comments;
        this.projects = projects;
        this.teams = teams;
        this.role = role;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user_name) {
        this.userName = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String user_email) {
        this.mail = user_email;
    }

    @OneToOne
    @JoinColumn(name = "org_id")
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    @OneToMany(mappedBy = "commenter")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @ManyToMany(mappedBy = "users")
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @ManyToMany(mappedBy = "users")
    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "OrganisationMembers [user_name=" + userName + ", password=" + password + ", user_email=" + mail
                + ", organisation=" + organisation + ", comments=" + comments + ", projects=" + projects + ", teams="
                + teams + ", role=" + role + "]";
    }



}