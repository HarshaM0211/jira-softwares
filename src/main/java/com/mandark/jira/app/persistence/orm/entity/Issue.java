package com.mandark.jira.app.persistence.orm.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.mandark.jira.app.enums.IssuePriority;
import com.mandark.jira.app.enums.IssueStatus;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "issues",
        indexes = {@Index(columnList = "project_id", name = "project_id"),
                @Index(columnList = "issue_key", name = "issue_key"),
                @Index(columnList = "parent_issue_id", name = "parent_issue_id"),
                @Index(columnList = "reported_by", name = "reported_by"),
                @Index(columnList = "version_str", name = "version_str"), @Index(columnList = "type", name = "type")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id", "issue_key"})})
public class Issue extends JpaAuditEntity {

    // Fields Lables
    // ------------------------------------------------------------------------

    public static final String PROP_PROJECT = "project";

    public static final String PROP_ISSUE_KEY = "issueKey";

    public static final String PROP_TYPE = "type";

    public static final String PROP_ASSIGNEE = "assignee";

    public static final String PROP_STATUS = "status";

    public static final String PROP_PARENT_ISSUE = "parentIssue";

    public static final String PROP_SPRINT = "sprint";

    public static final String PROP_REPORTED_BY = "reportedBy";

    public static final String PROP_START_DATE = "startDate";

    public static final String PROP_END_DATE = "endDate";

    public static final String PROP_VERSION_STR = "versionStr";

    public static final String PROP_PRIORITY = "priority";

    public static final String PROP_IS_ACTIVE = "isActive";

    // Fields
    // ------------------------------------------------------------------------

    // Info
    private Project project;
    private String issueKey;
    private String summary;

    // Dates
    private Long startTimeStamp;
    private Long endTimeStamp;

    // Tags
    private IssueType type;
    private String versionStr;
    private IssuePriority priority;
    private String label;

    // Extra contents
    private List<Attachment> attachments;
    private List<Comment> comments;

    private User assignee;// (mem_id)

    private IssueStatus status;

    private Issue parentIssue;// (Issue ID of this table)

    private User reportedBy;// (user_id)

    private Boolean isActive = true;

    // Constructors
    // ------------------------------------------------------------------------

    // Default
    public Issue() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(project)) {
            throw new ValidationException("#validate :: project is BLANK");
        }

        if (Objects.isNull(issueKey)) {
            throw new ValidationException("#validate :: issueKey is BLANK");
        }

        if (Objects.isNull(summary)) {
            throw new ValidationException("#validate :: description is BLANK");
        }

        if (Objects.isNull(type)) {
            throw new ValidationException("#validate :: type is BLANK");
        }

        if (Objects.isNull(status)) {
            throw new ValidationException("#validate :: status is BLANK");
        }

        if (Objects.isNull(priority)) {
            throw new ValidationException("#validate :: priority is BLANK");
        }

    }

    // Getters and Setters
    // -------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(name = "issue_key", nullable = false)
    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issue_key) {
        this.issueKey = issue_key;
    }

    @Column(name = "summary", nullable = false)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Column(name = "version_str")
    public String getVersionStr() {
        return versionStr;
    }

    public void setVersionStr(String versionStr) {
        this.versionStr = versionStr;
    }

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    @OneToOne
    @JoinColumn(name = "assignee")
    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "parent_issue_id")
    public Issue getParentIssue() {
        return parentIssue;
    }

    public void setParentIssue(Issue parent_issue) {
        this.parentIssue = parent_issue;
    }

    @OneToOne
    @JoinColumn(name = "reported_by")
    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reported_by) {
        this.reportedBy = reported_by;
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

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    @Column(name = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @OneToMany(mappedBy = "issue", cascade = CascadeType.REMOVE)
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> Attachment) {
        this.attachments = Attachment;
    }

    @OneToMany(mappedBy = "issue", cascade = CascadeType.REMOVE)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) default 1")
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Object Methods
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Issues [project=" + project.getId() + ", issue_key=" + issueKey + ", summary=" + summary + ", type="
                + type + ", assignee=" + assignee + ", status=" + status + ", parent_issue_id=" + parentIssue
                + ", reported_by=" + reportedBy.getId() + "]";
    }
}
