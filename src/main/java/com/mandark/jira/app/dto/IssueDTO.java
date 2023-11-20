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

    private final String issue_key;

    private final String description;// (name)

    private final IssueType type;

    private final UserDTO assignee;// (mem_id)

    private final IssueStatus status;

    private final int parent_issue_id;// (Issue ID of this table)

    private final List<SprintDTO> sprints;

    private final UserDTO reported_by;// (mem_id)

    private final Date start_date;

    private final Date end_date;

    private final String versionStr;

    private final IssuePriority priority;

    private final String label;

    private final List<AttachmentDTO> attachments;

    private final List<CommentDTO> comments;


    // Constructors
    // ------------------------------------------------------------------------

    public IssueDTO(Issue e) {
        super(e);
        this.issue_key = e.getIssueKey();
        this.description = e.getDescription();
        this.type = e.getType();
        this.assignee = new UserDTO(e.getAssignee());
        this.status = e.getStatus();
        this.parent_issue_id = e.getParentIssueId();
        
        List<SprintDTO> sprintDTOs = new ArrayList<>();
        for(Sprint s : e.getSprint()) {
            sprintDTOs.add(new SprintDTO(s));
        }
        this.sprints = sprintDTOs;
        this.reported_by = new UserDTO(e.getReportedBy());
        this.start_date = e.getStartDate();
        this.end_date = e.getEndDate();
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

    public String getIssue_key() {
        return issue_key;
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

    public int getParent_issue_id() {
        return parent_issue_id;
    }

    public List<SprintDTO> getSprint() {
        return sprints;
    }

    public UserDTO getReported_by() {
        return reported_by;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
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
        return "IssuesDTO [issue_key=" + issue_key + ", description=" + description + ", type=" + type + ", assignee="
                + assignee + ", status=" + status + ", parent_issue_id=" + parent_issue_id + ", sprint=" + sprints
                + ", reported_by=" + reported_by + ", start_date=" + start_date + ", end_date=" + end_date
                + ", version=" + versionStr + ", priority=" + priority + ", label=" + label + ", attachments="
                + attachments + ", comments=" + comments + "]";
    }



}
