package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityDTO;



public class TeamDTO extends EntityDTO<Team> {

    // Fields
    // ------------------------------------------------------------------------

    private final String name;

    private final UserDTO teamLeader; // (org_mem_id)

    private final List<UserDTO> users;

    // Constructors
    // ------------------------------------------------------------------------

    public TeamDTO(Team e) {
        super(e);
        this.name = e.getName();
        this.teamLeader = Objects.isNull(e.getTeamLeader()) ? null : new UserDTO(e.getTeamLeader());

        List<UserDTO> userDTOs = new ArrayList<UserDTO>();
        for (User n : e.getUsers()) {
            UserDTO userDto = Objects.isNull(n) ? null : new UserDTO(n);
            userDTOs.add(userDto);
        }

        this.users = userDTOs;
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public UserDTO getTeamLeader() {
        return teamLeader;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "TeamDTO [name=" + name + ", teamLeader=" + teamLeader + ", users=" + users + "]";
    }

}