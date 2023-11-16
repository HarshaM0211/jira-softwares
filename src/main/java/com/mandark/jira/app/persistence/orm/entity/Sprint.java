package com.mandark.jira.app.persistence.orm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.enums.SprintStatus;
import com.mandark.jira.app.persistence.orm.JpaAuditEntity;


@Entity
@Table(name = "sprints")
public class Sprint extends JpaAuditEntity {

    private Organisation organisation;

    private Project project;

    private String sprintKey;

    private Date startDate;

    private Date endDate;

    private List<Issue> issues;

    private SprintStatus status;

    // Constructors
    // ------------------------------------------------------------------------

    // Defaults
    public Sprint() {
        super();
    }

    public Sprint(Organisation organisation, Project project, String sprint_key, Date start_date, Date end_date,
            List<Issue> issues, SprintStatus status) {
        super();
        this.organisation = organisation;
        this.project = project;
        this.sprintKey = sprint_key;
        this.startDate = start_date;
        this.endDate = end_date;
        this.issues = issues;
        this.status = status;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "org_id")
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    @ManyToOne
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(name = "sprint_key")
    public String getSprintKey() {
        return sprintKey;
    }

    public void setSprintKey(String sprint_key) {
        this.sprintKey = sprint_key;
    }

    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date start_date) {
        this.startDate = start_date;
    }

    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date end_date) {
        this.endDate = end_date;
    }

    @ManyToMany(mappedBy = "sprint")
    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Sprint [organisation=" + organisation + ", project=" + project + ", sprintKey=" + sprintKey
                + ", startDate=" + startDate + ", endDate=" + endDate + ", issues=" + issues + ", status=" + status
                + "]";
    }

}
