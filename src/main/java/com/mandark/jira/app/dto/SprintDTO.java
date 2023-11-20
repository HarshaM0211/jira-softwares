package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mandark.jira.app.enums.SprintStatus;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;


public class SprintDTO extends EntityDTO<Sprint> {

    private final String sprint_key;

    private final Date start_date;

    private final Date end_date;

    private final List<IssueDTO> issues;

    private final SprintStatus status;


    // Constructors
    // ------------------------------------------------------------------------

    public SprintDTO(Sprint e) {
        super(e);

        this.sprint_key = e.getSprintKey();
        this.start_date = e.getStartDate();
        this.end_date = e.getEndDate();

        List<IssueDTO> issuesDTO = new ArrayList<>();
        for (Issue n : e.getIssues()) {
            issuesDTO.add(new IssueDTO(n));
        }

        this.issues = issuesDTO;
        this.status = e.getStatus();
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getSprint_key() {
        return sprint_key;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public List<IssueDTO> getIssues() {
        return issues;
    }

    public SprintStatus getStatus() {
        return status;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "SprintsDTO [sprint_key=" + sprint_key + ", start_date=" + start_date + ", end_date=" + end_date
                + ", issues=" + issues + ", status=" + status + "]";
    }


}
