package com.mandark.jira.app.persistence.orm.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;



@Entity
@Table(name = "comments")
public class Comment extends JpaAuditEntity {

    private Issue issue;

    private User commenter;// (org_member)

    private String comment;


    // Constructors
    // -------------------------------------------------------------------------

    // Default
    public Comment() {
        super();
    }


    public Comment(Issue issue, User org_member, String comment) {
        super();
        this.issue = issue;
        this.commenter = org_member;
        this.comment = comment;
    }



    // Getters and Setters
    // -------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "issue_id")
    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @ManyToOne
    @JoinColumn(name = "commenter_id")
    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User org_member) {
        this.commenter = org_member;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Object Methods
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Comments [issue=" + issue + ", org_member=" + commenter + ", comment=" + comment + "]";
    }



}
