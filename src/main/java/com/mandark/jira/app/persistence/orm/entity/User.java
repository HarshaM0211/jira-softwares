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

import com.mandark.jira.app.enums.UserRole;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "users",
        indexes = {@Index(columnList = "org_id", name = "org_id"), @Index(columnList = "role", name = "role")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User extends JpaAuditEntity {

    // Field Lables
    // ------------------------------------------------------------------------

    public static final String PROP_FIRST_NAME = "firstName";

    public static final String PROP_EMAIL = "email";

    public static final String PROP_ORGANISATION = "organisation";

    public static final String PROP_PHONE = "phone";

    public static final String PROP_ROLE = "role";

    // Fields
    // ------------------------------------------------------------------------

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private Organisation organisation;

    private List<Comment> comments;

    private List<Project> projects;

    private UserRole role;

    private String phone;

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

        if (Objects.isNull(firstName)) {
            throw new ValidationException("#validate :: firstName is BLANK");
        }

        if (Objects.isNull(password)) {
            throw new ValidationException("#validate :: password is BLANK");
        }

        if (Objects.isNull(email)) {
            throw new ValidationException("#validate :: mail is BLANK");
        }

        if (Objects.isNull(phone)) {
            throw new ValidationException("#validate :: phone is BLANK");
        }

    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne
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

    @Column(name = "role")
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Column(name = "phone", unique = true, nullable = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Object Methods
    // ------------------------------------------------------------------------



    @Override
    public String toString() {
        return "Users [user_name=" + firstName + lastName + ", user_email=" + email + ", organisation=" + organisation
                + ", role=" + role + "]";
    }



}
