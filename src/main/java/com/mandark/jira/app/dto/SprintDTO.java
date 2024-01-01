package com.mandark.jira.app.dto;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;


public class SprintDTO extends EntityDTO<Sprint> {

    public static final String DTO_DATE_FORMAT = "dd MMM yyyy";

    // Fields
    // -------------------------------------------------------------------------

    private final String sprintKey;

    private final String startDate;

    private final String endDate;

    private final String status;

    // Constructors
    // ------------------------------------------------------------------------

    public SprintDTO(Sprint e) {
        super(e);

        this.sprintKey = e.getSprintKey();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DTO_DATE_FORMAT);

        this.startDate = Objects.isNull(e.getStartDate()) ? null : e.getStartDate().toLocalDate().format(formatter);
        this.endDate = Objects.isNull(e.getEndDate()) ? null : e.getEndDate().toLocalDate().format(formatter);
        this.status = e.getStatus().toString();
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getSprintKey() {
        return sprintKey;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
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
