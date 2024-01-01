package com.mandark.jira.app.dto;

import java.sql.Timestamp;

import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;


public class SprintDTO extends EntityDTO<Sprint> {

    // Fields
    // -------------------------------------------------------------------------

    private final String sprintKey;

    private final Timestamp startDate;

    private final Timestamp endDate;

    private final String status;

    // Constructors
    // ------------------------------------------------------------------------

    public SprintDTO(Sprint e) {
        super(e);

        this.sprintKey = e.getSprintKey();
        this.startDate = e.getStartDate();
        this.endDate = e.getEndDate();
        this.status = e.getStatus().toString();
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getSprintKey() {
        return sprintKey;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "SprintDTO [sprintId = " + id + ", sprintKey=" + sprintKey + "]";
    }

}
