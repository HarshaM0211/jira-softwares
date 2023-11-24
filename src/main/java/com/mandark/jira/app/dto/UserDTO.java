package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mandark.jira.app.enums.UserRole;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityDTO;


public class UserDTO extends EntityDTO<User> {

    // Fields
    // ------------------------------------------------------------------------

    private final String userName; // firstName + lastName

    private final String email;

    private final List<ProjectDTO> projects;

    private final UserRole role;

    private final OrganisationDTO org;

    private final String phone;

    // Constructors
    // ------------------------------------------------------------------------

    public UserDTO(User e) {
        super(e);
        this.userName = e.getFirstName() + e.getLastName();
        this.email = e.getEmail();

        List<ProjectDTO> projectsDTO = new ArrayList<>();
        for (Project p : e.getProjects()) {
            ProjectDTO projDto = Objects.isNull(p) ? null : new ProjectDTO(p);
            projectsDTO.add(projDto);
        }
        this.projects = projectsDTO;

        this.role = e.getRole();
        this.org = Objects.isNull(e.getOrganisation()) ? null : new OrganisationDTO(e.getOrganisation());
        this.phone = e.getPhone();
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

    public UserRole getRole() {
        return role;
    }

    public OrganisationDTO getOrg() {
        return org;
    }

    public String getPhone() {
        return phone;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "UserDTO [userName=" + userName + ", email=" + email + ", projects=" + projects + ", teams=" + teams
                + ", role=" + role + ", organisation=" + org.getId() + ", phone=" + phone + "]";
    }
}
