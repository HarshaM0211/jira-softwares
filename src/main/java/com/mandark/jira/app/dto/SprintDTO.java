package com.mandark.jira.app.dto;

import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;


public class SprintDTO extends EntityDTO<Sprint> {

    // Fields
    // -------------------------------------------------------------------------

    private final String sprintKey;

    private final long startTimeStamp;

    private final long endTimeStamp;

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

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public long getEndTimeStamp() {
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
