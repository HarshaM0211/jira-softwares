package com.mandark.jira.app.persistence.orm.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mandark.jira.app.enums.IssuePriority;
import com.mandark.jira.app.enums.IssueStatus;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "issues")
public class Issue extends JpaAuditEntity {

    // @ManyToOne
    // @JoinColumn(name = "org_id")
    // private Organisations organisation;

    private Project project;

    private String issueKey;

    private String description;// (name)

    private IssueType type;

    private User assignee;// (mem_id)

    private IssueStatus status;

    private int parentIssueId;// (Issue ID of this table)

    private List<Sprint> sprint;

    private User reportedBy;// (mem_id)

    private Date startDate;

    private Date endDate;

    private String versionStr;

    private IssuePriority priority;

    private String label;

    private List<Attachment> attachments;

    private List<Comment> comments;


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

        super.validate();

        if (Objects.isNull(project)) {
            throw new ValidationException("#validate :: project is BLANK");
        }

        if (Objects.isNull(issueKey)) {
            throw new ValidationException("#validate :: issueKey is BLANK");
        }

        if (Objects.isNull(description)) {
            throw new ValidationException("#validate :: description is BLANK");
        }

        if (Objects.isNull(type)) {
            throw new ValidationException("#validate :: type is BLANK");
        }

        if (Objects.isNull(reportedBy)) {
            throw new ValidationException("#validate :: reportedBy is BLANK");
        }

    }

    // Getters and Setters
    // -------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(name = "issue_key")
    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issue_key) {
        this.issueKey = issue_key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "version_str")
    public String getVersionStr() {
        return versionStr;
    }

    public void setVersionStr(String versionStr) {
        this.versionStr = versionStr;
    }

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

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    @Column(name = "parent_issue_id")
    public int getParentIssueId() {
        return parentIssueId;
    }

    public void setParentIssueId(int parent_issue_id) {
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
    @JoinColumn(name = "reported_by")
    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reported_by) {
        this.reportedBy = reported_by;
    }

    @Column(name = "strat_date")
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

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

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

    // Object Methods
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Issues [project=" + project + ", issue_key=" + issueKey + ", description=" + description + ", type="
                + type + ", assignee=" + assignee + ", status=" + status + ", parent_issue_id=" + parentIssueId
                + ", sprint=" + sprint + ", reported_by=" + reportedBy + ", start_date=" + startDate + ", end_date="
                + endDate + ", version=" + versionStr + ", priority=" + priority + ", label=" + label + ", Attachment="
                + attachments + ", comments_ids=" + comments + "]";
    }



}
