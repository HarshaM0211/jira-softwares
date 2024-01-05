package com.mandark.jira.app.persistence.orm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "sprints_issues", indexes = {@Index(columnList = "sprint_id", name = "sprint_id"),
        @Index(columnList = "issue_id", name = "issue_id")})
public class SprintIssue extends JpaAuditEntity {

    // Field Lables
    // ------------------------------------------------------------------------

    public static final String PROP_SPRINT = "sprint";

    public static final String PROP_ISSUE = "issue";

    public static final String PROP_IS_LATEST = "isLatest";

    // Fields
    // ------------------------------------------------------------------------

    public Sprint sprint;

    public Issue issue;

    public Boolean isLatest;


    // Constructors
    // ------------------------------------------------------------------------

    public SprintIssue() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(sprint)) {
            throw new ValidationException("#validate :: sprint is BLANK");
        }

        if (Objects.isNull(issue)) {
            throw new ValidationException("#validate :: issue is BLANK");
        }
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = false)
    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Column(name = "is_latest", nullable = false, columnDefinition = "TINYINT(1) default 0")
    public Boolean getIsLatest() {
        return isLatest;
    }

    public void setIsLatest(Boolean isLatest) {
        this.isLatest = isLatest;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "SprintIssue [sprint=" + sprint + ", issue=" + issue + ", isLatest=" + isLatest + "]";
    }
}
