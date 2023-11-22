package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mandark.jira.app.enums.IssuePriority;
import com.mandark.jira.app.enums.IssueStatus;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.persistence.orm.entity.Attachment;
import com.mandark.jira.app.persistence.orm.entity.Comment;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;


public class IssueDTO extends EntityDTO<Issue> {

    // Fields
    // -------------------------------------------------------------------------

    private final String issueKey;

    private final String description;// (name)

    private final IssueType type;

    private final UserDTO assignee;// (mem_id)

    private final IssueStatus status;

    private final int parentIssueId;// (Issue ID of this table)

    private final List<SprintDTO> sprints;

    private final UserDTO reportedBy;// (mem_id)

    private final Date startDate;

    private final Date endDate;

    private final String versionStr;

    private final IssuePriority priority;

    private final String label;

    private final List<AttachmentDTO> attachments;

    private final List<CommentDTO> comments;

    // Constructors
    // ------------------------------------------------------------------------

    public IssueDTO(Issue e) {
        super(e);
        this.issueKey = e.getIssueKey();
        this.description = e.getDescription();
        this.type = e.getType();
        this.assignee = new UserDTO(e.getAssignee());
        this.status = e.getStatus();
        this.parentIssueId = e.getParentIssueId();

        List<SprintDTO> sprintDTOs = new ArrayList<>();
        for (Sprint s : e.getSprint()) {
            sprintDTOs.add(new SprintDTO(s));
        }
        this.sprints = sprintDTOs;
        this.reportedBy = new UserDTO(e.getReportedBy());
        this.startDate = e.getStartDate();
        this.endDate = e.getEndDate();
        this.versionStr = e.getVersionStr();
        this.priority = e.getPriority();
        this.label = e.getLabel();

        List<AttachmentDTO> attachmentsDTO = new ArrayList<>();
        for (Attachment a : e.getAttachments()) {
            attachmentsDTO.add(new AttachmentDTO(a));
        }
        this.attachments = attachmentsDTO;

        List<CommentDTO> commentsDTO = new ArrayList<>();
        for (Comment c : e.getComments()) {
            commentsDTO.add(new CommentDTO(c));
        }
        this.comments = commentsDTO;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getIssueKey() {
        return issueKey;
    }

    public String getDescription() {
        return description;
    }

    public IssueType getType() {
        return type;
    }

    public UserDTO getAssignee() {
        return assignee;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public int getParentIssueId() {
        return parentIssueId;
    }

    public List<SprintDTO> getSprints() {
        return sprints;
    }

    public UserDTO getReportedBy() {
        return reportedBy;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getVersionStr() {
        return versionStr;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public String getLabel() {
        return label;
    }

    public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }


    // Object Methods
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "IssueDTO [issueKey=" + issueKey + ", description=" + description + ", type=" + type + ", assignee="
                + assignee + ", status=" + status + ", parentIssueId=" + parentIssueId + ", reportedBy=" + reportedBy
                + ", startDate=" + startDate + ", endDate=" + endDate + ", versionStr=" + versionStr + ", priority="
                + priority + ", label=" + label + "]";
    }

}
