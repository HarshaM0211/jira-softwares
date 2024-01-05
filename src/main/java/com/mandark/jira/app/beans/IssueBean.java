package com.mandark.jira.app.beans;

import java.util.Objects;

import com.mandark.jira.app.enums.IssuePriority;
import com.mandark.jira.app.enums.IssueStatus;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.EntityBean;


public class IssueBean implements EntityBean<Issue> {

    private String summary;

    private Long startTimeStamp;

    private Long endTimeStamp;

    private String typeStr;

    private String versionStr;

    private String label;

    private Integer assigneeId;

    private String statusStr;

    private Integer parentIssueId;

    private Integer sprintId;

    private String priorityStr;


    // Methods
    // ------------------------------------------------------------------------

    public IssueType getIssueType() {
        return Objects.isNull(typeStr) ? null : IssueType.valueOf(typeStr);
    }

    public IssueStatus getIssueStatus() {
        return Objects.isNull(statusStr) ? null : IssueStatus.valueOf(statusStr);
    }

    public IssuePriority getIssuePriority() {
        return Objects.isNull(priorityStr) ? null : IssuePriority.valueOf(priorityStr);
    }

    // Constructors
    // ------------------------------------------------------------------------


    // Getters and Setters
    // ------------------------------------------------------------------------

    public IssueBean() {
        super();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getVersionStr() {
        return versionStr;
    }

    public void setVersionStr(String versionStr) {
        this.versionStr = versionStr;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public Integer getParentIssueId() {
        return parentIssueId;
    }

    public void setParentIssueId(Integer parentIssueId) {
        this.parentIssueId = parentIssueId;
    }

    public Integer getSprintId() {
        return sprintId;
    }

    public void setSprintId(Integer sprintId) {
        this.sprintId = sprintId;
    }

    public String getPriorityStr() {
        return priorityStr;
    }

    public void setPriorityStr(String priorityStr) {
        this.priorityStr = priorityStr;
    }
}
