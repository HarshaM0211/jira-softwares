package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityBean;


public class SprintBean implements EntityBean<Sprint> {

    // Fields
    // ------------------------------------------------------------------------

    public String sprintKey;

    public String startDate;

    public String endDate;

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getSprintKey() {
        return sprintKey;
    }

    public void setSprintKey(String sprintKey) {
        this.sprintKey = sprintKey;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
