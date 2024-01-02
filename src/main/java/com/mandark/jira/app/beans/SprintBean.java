package com.mandark.jira.app.beans;

import java.sql.Timestamp;

import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityBean;


public class SprintBean implements EntityBean<Sprint> {

    // Fields
    // ------------------------------------------------------------------------

    public String sprintKey;

    public Timestamp startDate;

    public Timestamp endDate;

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getSprintKey() {
        return sprintKey;
    }

    public void setSprintKey(String sprintKey) {
        this.sprintKey = sprintKey;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
