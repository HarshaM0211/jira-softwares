package com.mandark.jira.app.dto;

import com.mandark.jira.app.persistence.orm.entity.Comment;
import com.mandark.jira.spi.app.EntityDTO;


public class CommentDTO extends EntityDTO<Comment> {

    // Fields
    // -------------------------------------------------------------------------

    private final IssueDTO issue;

    private final UserDTO commenter;// (org_member)

    private final String comment;


    // Constructors
    // -------------------------------------------------------------------------

    public CommentDTO(Comment e) {
        super(e);
        this.issue = new IssueDTO(e.getIssue());
        this.commenter = new UserDTO(e.getCommenter());
        this.comment = e.getComment();
    }

    // Getters and Setters
    // -------------------------------------------------------------------------

    public IssueDTO getIssue() {
        return issue;
    }

    public UserDTO getCommenter() {
        return commenter;
    }

    public String getComment() {
        return comment;
    }

    // Object Methods
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "CommentDTO [issue=" + issue + ", commenter=" + commenter + ", comment=" + comment + "]";
    }


}
