package com.mandark.jira.app.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.EntityDTO;



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
            SprintDTO sprintDto = Objects.isNull(s) ? null : new SprintDTO(s);
            sprintDTOs.add(sprintDto);
        }
        this.sprints = sprintDTOs;

        final List<IssueDTO> issueDTOs = new ArrayList<>();
        for (Issue i : e.getIssues()) {
            IssueDTO issueDto = Objects.isNull(i) ? null : new IssueDTO(i);
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
