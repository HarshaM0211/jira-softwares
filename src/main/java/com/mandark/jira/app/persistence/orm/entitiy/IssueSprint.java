package com.mandark.jira.app.persistence.orm.entitiy;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;



@Entity
@Table(name = "issues_sprints`")
public class IssueSprint extends JpaAuditEntity {

    private int sprintId;

    private int issueId;

    // Constructors
    // ------------------------------------------------------------------------

    // Default
    public IssueSprint() {
        super();
    }

    public IssueSprint(int sprint_id, int issue_id) {
        super();
        this.sprintId = sprint_id;
        this.issueId = issue_id;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public int getSprintId() {
        return sprintId;
    }

    public void setSprintId(int sprint_id) {
        this.sprintId = sprint_id;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issue_id) {
        this.issueId = issue_id;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Issues_Sprints [sprint_id=" + sprintId + ", issue_id=" + issueId + "]";
    }


}
