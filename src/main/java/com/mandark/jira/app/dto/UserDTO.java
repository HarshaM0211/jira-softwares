package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.mandark.jira.app.enums.UserRole;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityDTO;
import com.mandark.jira.spi.util.Values;


public class UserDTO extends EntityDTO<User> {

    // Fields
    // ------------------------------------------------------------------------

    private final String userName; // firstName + lastName

    private final String email;

    private final List<ProjectDTO> projects;

    private final UserRole role;

    private final String phone;

    // Constructors
    // ------------------------------------------------------------------------

    public UserDTO(User e) {
        super(e);
        this.userName = e.getFirstName() + e.getLastName();
        this.email = e.getEmail();

        final List<ProjectDTO> projectsDTO = new ArrayList<>();
        for (Project p : e.getProjects()) {
            ProjectDTO projDto = Values.get(p, ProjectDTO::new);
            projectsDTO.add(projDto);
        }
        this.projects = projectsDTO;

        this.role = e.getRole();
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

    public String getPhone() {
        return phone;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "UserDTO [userName=" + userName + ", email=" + email + ", projects=" + projects + ", role=" + role
                + ", phone=" + phone + "]";
    }
}
