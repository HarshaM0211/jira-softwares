package com.mandark.jira.app.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mandark.jira.app.enums.SprintStatus;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;
import com.mandark.jira.spi.util.Values;


public class SprintDTO extends EntityDTO<Sprint> {

    // Fields
    // -------------------------------------------------------------------------

    private final String sprintKey;

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    private final List<IssueDTO> issues;

    private final SprintStatus status;

    // Constructors
    // ------------------------------------------------------------------------

    public SprintDTO(Sprint e) {
        super(e);

        this.sprintKey = e.getSprintKey();
        this.startDate = e.getStartDate();
        this.endDate = e.getEndDate();

        final List<IssueDTO> issuesDTO = new ArrayList<>();
        for (Issue n : e.getIssues()) {
            IssueDTO issueDto = Values.get(n, IssueDTO::new);
            issuesDTO.add(issueDto);
        }

        this.issues = issuesDTO;
        this.status = e.getStatus();
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getSprintKey() {
        return sprintKey;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
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
        return "SprintDTO [sprintKey=" + sprintKey + ", startDate=" + startDate + ", endDate=" + endDate + ", issues="
                + issues + ", status=" + status + "]";
    }

}
