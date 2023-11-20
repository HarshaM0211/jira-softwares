package com.mandark.jira.app.persistence.orm.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mandark.jira.app.enums.UserRole;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "users")
public class User extends JpaAuditEntity {

    private String userName;

    private String password;

    private String mail;

    private UserRole role;

    private Organisation organisation;

    private List<Comment> comments;

    private List<Project> projects;

    private List<Team> teams;


    // Constructors
    // ------------------------------------------------------------------------

    // Default
    public User() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        super.validate();

        if (Objects.isNull(userName)) {
            throw new ValidationException("#validate :: userName is BLANK");
        }

        if (Objects.isNull(password)) {
            throw new ValidationException("#validate :: password is BLANK");
        }

        if (Objects.isNull(mail)) {
            throw new ValidationException("#validate :: mail is BLANK");
        }

        if (Objects.isNull(organisation)) {
            throw new ValidationException("#validate :: organisation is BLANK");
        }

    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    @Column(name = "user_name")
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
