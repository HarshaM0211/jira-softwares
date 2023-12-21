package com.mandark.jira.app.persistence.orm.entity;

import java.time.LocalDateTime;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.mandark.jira.app.enums.IsIssueDeleted;
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
                @Index(columnList = "version_str", name = "version_str")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id", "issue_key"})})
public class Issue extends JpaAuditEntity {

    // Fields Lables
    // ------------------------------------------------------------------------

    public static final String PROP_PROJECT = "project";

    public static final String PROP_ISSUE_KEY = "issueKey";

    public static final String PROP_TYPE = "type";

    public static final String PROP_ASSIGNEE = "assignee";

    public static final String PROP_STATUS = "status";

    public static final String PROP_PARENT_ISSUE_ID = "parentIssueId";

    public static final String PROP_SPRINT = "sprint";

    public static final String PROP_REPORTED_BY = "reportedBy";

    public static final String PROP_START_DATE = "startDate";

    public static final String PROP_END_DATE = "endDate";

    public static final String PROP_VERSION_STR = "versionStr";

    public static final String PROP_PRIORITY = "priority";

    // Fields
    // ------------------------------------------------------------------------

    // Info
    private Project project;
    private String issueKey;
    private String summary;

    // Dates
    private LocalDateTime startDate;
    private LocalDateTime endDate;

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

    private Integer parentIssueId;// (Issue ID of this table)

    private List<Sprint> sprint;

    private User reportedBy;// (user_id)

    private IsIssueDeleted isDeleted;

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

        if (Objects.isNull(reportedBy)) {
            throw new ValidationException("#validate :: reportedBy is BLANK");
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

    @Column(name = "parent_issue_id")
    public Integer getParentIssueId() {
        return parentIssueId;
    }

    public void setParentIssueId(Integer parent_issue_id) {
        this.parentIssueId = parent_issue_id;
    }

    @ManyToMany
    @JoinColumn(name = "sprint_id")
    public List<Sprint> getSprint() {
        return sprint;
    }

    public void setSprint(List<Sprint> sprint) {
        this.sprint = sprint;
    }

    @OneToOne
    @JoinColumn(name = "reported_by", nullable = false)
    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reported_by) {
        this.reportedBy = reported_by;
    }

    @Column(name = "strat_date")
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime start_date) {
        this.startDate = start_date;
    }

    @Column(name = "end_date")
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime end_date) {
        this.endDate = end_date;
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

    @OneToMany(mappedBy = "issue")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> Attachment) {
        this.attachments = Attachment;
    }

    @OneToMany(mappedBy = "issue")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Column(name = "is_deleted", columnDefinition = "varchar(3) default 'NO'")
    @Enumerated(EnumType.STRING)
    public IsIssueDeleted getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(IsIssueDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Object Methods
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Issues [project=" + project.getId() + ", issue_key=" + issueKey + ", summary=" + summary + ", type="
                + type + ", assignee=" + assignee + ", status=" + status + ", parent_issue_id=" + parentIssueId
                + ", reported_by=" + reportedBy.getId() + "]";
    }



}
