package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.mandark.jira.app.persistence.orm.entity.Attachment;
import com.mandark.jira.app.persistence.orm.entity.Comment;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.EntityDTO;
import com.mandark.jira.spi.util.Values;


public class IssueDTO extends EntityDTO<Issue> {

    // Fields
    // -------------------------------------------------------------------------

    private final String issueKey;

    private final String summary;// (name)

    private final String typeStr;

    private final UserDTO assignee;// (mem_id)

    private final String statusStr;

    private final Integer parentIssueId;// (Issue ID of this table)

    private final UserDTO reportedBy;// (mem_id)

    private final Long startDate;

    private final Long endDate;

    private final String versionStr;

    private final String priorityStr;

    private final String label;

    private final List<AttachmentDTO> attachments;

    private final List<CommentDTO> comments;

    // Constructors
    // ------------------------------------------------------------------------

    public IssueDTO(Issue e) {
        super(e);

        this.issueKey = e.getIssueKey();

        this.summary = e.getSummary();
        this.typeStr = e.getType().name(); // Type is NonNull

        this.assignee = Values.get(e.getAssignee(), UserDTO::new);

        this.statusStr = e.getStatus().name();
        this.parentIssueId = Values.get(e.getParentIssue(), Issue::getId);

        this.reportedBy = Values.get(e.getReportedBy(), UserDTO::new);

        this.startDate = e.getStartTimeStamp();
        this.endDate = e.getEndTimeStamp();

        this.versionStr = e.getVersionStr();
        this.priorityStr = e.getPriority().name();
        this.label = e.getLabel();

        final List<AttachmentDTO> attachmentsDTO = new ArrayList<>();
        for (Attachment a : e.getAttachments()) {
            AttachmentDTO attDto = Values.get(a, AttachmentDTO::new);
            attachmentsDTO.add(attDto);
        }
        this.attachments = attachmentsDTO;

        final List<CommentDTO> commentsDTO = new ArrayList<>();
        for (Comment c : e.getComments()) {
            CommentDTO cmntDto = Values.get(c, CommentDTO::new);
            commentsDTO.add(cmntDto);
        }
        this.comments = commentsDTO;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getIssueKey() {
        return issueKey;
    }

    public String getSummary() {
        return summary;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public UserDTO getAssignee() {
        return assignee;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public Integer getParentIssueId() {
        return parentIssueId;
    }

    public UserDTO getReportedBy() {
        return reportedBy;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public String getVersionStr() {
        return versionStr;
    }

    public String getPriorityStr() {
        return priorityStr;
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
        return "IssueDTO [issueKey=" + issueKey + ", summary=" + summary + ", type=" + typeStr + ", assignee="
                + assignee + ", status=" + statusStr + ", parentIssueId=" + parentIssueId + ", reportedBy=" + reportedBy
                + ", startDate=" + startDate + ", endDate=" + endDate + ", versionStr=" + versionStr + ", priority="
                + priorityStr + ", label=" + label + "]";
    }

}
