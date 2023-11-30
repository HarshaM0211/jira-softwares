package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;
import com.mandark.jira.spi.util.Values;



public class ProjectDTO extends EntityDTO<Project> {

    // Fields
    // ------------------------------------------------------------------------

    private final String projectKey;

    private final String name;

    private final String description;

    private final List<SprintDTO> sprints;

    private final List<IssueDTO> issues;

    // Constructors
    // ------------------------------------------------------------------------

    public ProjectDTO(Project e) {
        super(e);

        this.projectKey = e.getProjectKey();
        this.name = e.getName();
        this.description = e.getDescription();

        final List<SprintDTO> sprintDTOs = new ArrayList<>();
        for (Sprint s : e.getSprints()) {
            SprintDTO sprintDto = Values.get(s, SprintDTO::new);
            sprintDTOs.add(sprintDto);
        }
        this.sprints = sprintDTOs;

        final List<IssueDTO> issueDTOs = new ArrayList<>();
        for (Issue iss : e.getIssues()) {
            IssueDTO issueDto = Values.get(iss, IssueDTO::new);
            issueDTOs.add(issueDto);
        }
        this.issues = issueDTOs;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getProjectKey() {
        return projectKey;
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

    public List<IssueDTO> getIssues() {
        return issues;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ProjectDTO [projectKey=" + projectKey + ", name=" + name + ", description=" + description + ", sprints="
                + sprints + ", issues=" + issues + "]";
    }
}
