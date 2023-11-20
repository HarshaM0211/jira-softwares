package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityDTO;



public class TeamDTO extends EntityDTO<Team> {

    // Fields
    // ------------------------------------------------------------------------

    private final String name;

    private final UserDTO team_leader; // (org_mem_id)

    private final List<UserDTO> org_members;

    // Constructors
    // ------------------------------------------------------------------------

    public TeamDTO(Team e) {
        super(e);
        this.name = e.getName();
        this.team_leader = new UserDTO(e.getTeamLeader());

        List<UserDTO> org_membersDTO = new ArrayList<UserDTO>();
        for (User n : e.getUsers()) {
            org_membersDTO.add(new UserDTO(n));
        }

        this.org_members = org_membersDTO;
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getName() {
        return name;
    }


    public UserDTO getTeam_leader() {
        return team_leader;
    }


    public List<UserDTO> getOrg_members() {
        return org_members;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "TeamsDTO [name=" + name + ", team_leader=" + team_leader + ", org_members=" + org_members + "]";
    }


}
