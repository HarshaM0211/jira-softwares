package com.mandark.jira.app.persistence.orm.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "projects_users")
public class ProjectUser extends JpaAuditEntity {

    // Field Lables
    // ------------------------------------------------------------------------

    public static final String PROP_PROJECT = "project";

    public static final String PROP_USER = "user";

    // Fields
    // ------------------------------------------------------------------------

    private Project project;

    private User user;

    // Constructor
    // ------------------------------------------------------------------------

    public ProjectUser() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(project)) {
            throw new ValidationException("#validate :: project is BLANK");
        }

        if (Objects.isNull(user)) {
            throw new ValidationException("#validate :: user is BLANK");
        }
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ProjectUser [project=" + project + ", user=" + user + "]";
    }

}
