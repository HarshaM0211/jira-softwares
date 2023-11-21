package com.mandark.jira.app.persistence.orm.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.mandark.jira.app.enums.SprintStatus;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "sprints",
        indexes = {@Index(columnList = "project_id", name = "project_id"),
                @Index(columnList = "start_date", name = "start_date"),
                @Index(columnList = "end_date", name = "end_date"), @Index(columnList = "status", name = "status")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id", "sprint_key"})})
public class Sprint extends JpaAuditEntity {

    private Project project;

    private String sprintKey;

    private Date startDate;

    private Date endDate;

    private List<Issue> issues;

    private SprintStatus status;

    // Constructors
    // ------------------------------------------------------------------------

    // Default
    public Sprint() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(project)) {
            throw new ValidationException("#validate :: project is BLANK");
        }

        if (Objects.isNull(sprintKey)) {
            throw new ValidationException("#validate :: sprintKey is BLANK");
        }

        if (Objects.isNull(status)) {
            throw new ValidationException("#validate :: status is BLANK");
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

    @Column(name = "sprint_key", nullable = false)
    public String getSprintKey() {
        return sprintKey;
    }

    public void setSprintKey(String sprint_key) {
        this.sprintKey = sprint_key;
    }

    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date start_date) {
        this.startDate = start_date;
    }

    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date end_date) {
        this.endDate = end_date;
    }

    @ManyToMany(mappedBy = "sprint")
    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Sprint [project=" + project.getId() + ", sprintKey=" + sprintKey + ", status=" + status + "]";
    }

}
