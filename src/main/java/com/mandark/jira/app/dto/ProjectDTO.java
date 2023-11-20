package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityDTO;



public class ProjectDTO extends EntityDTO<Project> {

    // Fields
    // ------------------------------------------------------------------------
    private final String project_key;

    private final String name;

    private final String description;

    private final List<SprintDTO> sprints;

    private final List<UserDTO> org_members;

    private final List<IssueDTO> issues;

    // Constructors
    // ------------------------------------------------------------------------

    public ProjectDTO(Project e) {
        super(e);

        this.project_key = e.getProjectKey();
        this.name = e.getName();
        this.description = e.getDescription();

        List<SprintDTO> sprintsDTO = new ArrayList<>();
        for (Sprint s : e.getSprints()) {
            sprintsDTO.add(new SprintDTO(s));
        }
        this.sprints = sprintsDTO;

        List<UserDTO> org_membersDTO = new ArrayList<>();
        for (User om : e.getUsers()) {
            org_membersDTO.add(new UserDTO(om));
        }
        this.org_members = org_membersDTO;

        List<IssueDTO> issuesDTO = new ArrayList<>();
        for (Issue i : e.getIssues()) {
            issuesDTO.add(new IssueDTO(i));
        }
        this.issues = issuesDTO;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getProject_key() {
        return project_key;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public List<SprintDTO> getSprints() {
        return sprints;
    }


    public List<UserDTO> getOrg_members() {
        return org_members;
    }


    public List<IssueDTO> getIssues() {
        return issues;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ProjectsDTO [project_key=" + project_key + ", name=" + name + ", description=" + description
                + ", sprints=" + sprints + ", org_members=" + org_members + ", issues=" + issues + "]";
    }



}
