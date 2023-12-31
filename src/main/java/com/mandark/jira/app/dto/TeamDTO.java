package com.mandark.jira.app.dto;

import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.spi.app.EntityDTO;
import com.mandark.jira.spi.util.Values;



public class TeamDTO extends EntityDTO<Team> {

    // Fields
    // ------------------------------------------------------------------------

    private final String name;

    private final UserDTO teamLeader; // (org_mem_id)

    private final String description;

    // Constructors
    // ------------------------------------------------------------------------

    public TeamDTO(Team e) {
        super(e);
        this.name = e.getName();
        this.teamLeader = Values.get(e.getTeamLeader(), UserDTO::new);
        this.description = e.getDescription();
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public UserDTO getTeamLeader() {
        return teamLeader;
    }

    public String getDescription() {
        return description;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "TeamDTO [name=" + name + ", teamLeader=" + teamLeader + ", description=" + description + "]";
    }

}
