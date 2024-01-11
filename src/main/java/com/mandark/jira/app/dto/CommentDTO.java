package com.mandark.jira.app.dto;

import com.mandark.jira.app.persistence.orm.entity.Comment;
import com.mandark.jira.spi.app.EntityDTO;
import com.mandark.jira.spi.util.Values;


public class CommentDTO extends EntityDTO<Comment> {

    // Fields
    // -------------------------------------------------------------------------

    private final UserDTO commenter;// (org_member)

    private final String comment;


    // Constructors
    // -------------------------------------------------------------------------

    public CommentDTO(Comment e) {
        super(e);
        this.commenter = Values.get(e.getCommenter(), UserDTO::new);
        this.comment = e.getComment();
    }

    // Getters and Setters
    // -------------------------------------------------------------------------

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
        return "CommentDTO [Commenter=" + commenter + ", comment=" + comment + "]";
    }


}
