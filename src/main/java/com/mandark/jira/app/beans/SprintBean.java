package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityBean;


public class SprintBean implements EntityBean<Sprint> {

    // Fields
    // ------------------------------------------------------------------------

    public String sprintKey;

    public long startTimeStamp;

    public long endTimeStamp;

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getSprintKey() {
        return sprintKey;
    }

    public void setSprintKey(String sprintKey) {
        this.sprintKey = sprintKey;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

}
