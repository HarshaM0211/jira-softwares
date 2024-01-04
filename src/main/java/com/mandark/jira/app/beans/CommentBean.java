package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.Comment;
import com.mandark.jira.spi.app.EntityBean;


public class CommentBean implements EntityBean<Comment> {

    private String comment;

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
