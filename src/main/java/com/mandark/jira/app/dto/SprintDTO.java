package com.mandark.jira.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;


public class SprintDTO extends EntityDTO<Sprint> {

    // Fields
    // -------------------------------------------------------------------------

    private final String sprintKey;

    private final Long startTimeStamp;

    private final Long endTimeStamp;

    private final String status;

    // Constructors
    // ------------------------------------------------------------------------

    public SprintDTO(Sprint e) {
        super(e);

        this.sprintKey = e.getSprintKey();
        this.startTimeStamp = e.getStartTimeStamp();
        this.endTimeStamp = e.getEndTimeStamp();
        this.status = e.getStatus().toString();
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getSprintKey() {
        return sprintKey;
    }

    @JsonInclude
    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    @JsonInclude
    public Long getEndTimeStamp() {
        return endTimeStamp;
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
