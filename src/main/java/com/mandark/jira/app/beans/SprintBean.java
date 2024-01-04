package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityBean;


public class SprintBean implements EntityBean<Sprint> {

    // Fields
    // ------------------------------------------------------------------------

    public String sprintKey;

    public Long startTimeStamp;

    public Long endTimeStamp;

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getSprintKey() {
        return sprintKey;
    }

    public void setSprintKey(String sprintKey) {
        this.sprintKey = sprintKey;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

}
