package com.mandark.jira.app.persistence.orm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.mandark.jira.app.enums.SprintStatus;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "sprints", indexes = {@Index(columnList = "project_id", name = "project_id"),
        @Index(columnList = "start_time_stamp", name = "start_time_stamp"),
        @Index(columnList = "end_time_stamp", name = "end_time_stamp"), @Index(columnList = "status", name = "status")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id", "sprint_key"})})
public class Sprint extends JpaAuditEntity {

    // Field Lables
    // ------------------------------------------------------------------------

    public static final String PROP_PROJECT = "project";

    public static final String PROP_SPRINT_KEY = "sprintKey";

    public static final String PROP_START_DATE = "startDate";

    public static final String PROP_END_DATE = "endDate";

    public static final String PROP_STATUS = "status";

    // Fields
    // ------------------------------------------------------------------------

    private Project project;

    private String sprintKey;

    private Long startTimeStamp;

    private Long endTimeStamp;

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

    @Column(name = "start_time_stamp")
    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    @Column(name = "end_time_stamp")
    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
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
