package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.mandark.jira.app.enums.UserRole;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityDTO;


public class UserDTO extends EntityDTO<User> {

    // Fields
    // ------------------------------------------------------------------------

    private final String userName;

    private final String email;

    private final List<ProjectDTO> projects;

    private final List<TeamDTO> teams;

    private final UserRole role;

    private final Organisation org;

    // Constructors
    // ------------------------------------------------------------------------

    public UserDTO(User e) {
        super(e);
        this.userName = e.getFirstName() + e.getLastName();
        this.email = e.getEmail();

        List<ProjectDTO> projectsDTO = new ArrayList<>();
        for (Project p : e.getProjects()) {
            projectsDTO.add(new ProjectDTO(p));
        }
        this.projects = projectsDTO;

        List<TeamDTO> teamsDTO = new ArrayList<>();
        for (Team t : e.getTeams()) {
            teamsDTO.add(new TeamDTO(t));
        }
        this.teams = teamsDTO;

        this.role = e.getRole();
        this.org = e.getOrganisation();
    }

    // Getters
    // ------------------------------------------------------------------------

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public UserRole getRole() {
        return role;
    }

    public Organisation getOrg() {
        return org;
    }

    // Object Methods
    // ------------------------------------------------------------------------


    @Override
    public String toString() {
        return "UserDTO [userName=" + userName + ", email=" + email + ", projects=" + projects + ", teams=" + teams
                + ", role=" + role + ", organisation=" + org.getId() + "]";
    }
}
