package com.mandark.jira.app.persistence.orm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;



@Entity
@Table(name = "comments", indexes = {@Index(columnList = "issue_id", name = "issue_id"),
        @Index(columnList = "commenter_id", name = "commenter_id")})
public class Comment extends JpaAuditEntity {

    // Field Lables
    // ------------------------------------------------------------------------

    public static final String PROP_ISSUE = "issue";

    public static final String PROP_COMMENTER = "commenter";

    // Fields
    // ------------------------------------------------------------------------

    private Issue issue;

    private User commenter;// (user)

    private String comment;


    // Constructors
    // -------------------------------------------------------------------------

    // Default
    public Comment() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(issue)) {
            throw new ValidationException("#validate :: issue is BLANK");
        }

        if (Objects.isNull(commenter)) {
            throw new ValidationException("#validate :: commenter is BLANK");
        }

        if (Objects.isNull(comment)) {
            throw new ValidationException("#validate :: comment is BLANK");
        }

    }

    // Getters and Setters
    // -------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @ManyToOne
    @JoinColumn(name = "commenter_id", nullable = false)
    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User org_member) {
        this.commenter = org_member;
    }

    @Column(name = "comment", nullable = false)
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
        return "Comments [issue=" + issue.getId() + ", org_member=" + commenter.getFirstName() + commenter.getLastName()
                + ", comment=" + comment + "]";
    }

}
