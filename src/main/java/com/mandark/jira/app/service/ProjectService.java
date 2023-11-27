package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.ProjectBean;
import com.mandark.jira.app.dto.ProjectDTO;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.spi.app.service.EntityService;


public interface ProjectService extends EntityService<Integer, Project, ProjectDTO> {

    int create(final Integer orgId, ProjectBean entityBean);

    void update(final Integer ProjectId, ProjectBean projectBean);

    void addUser(final Integer userId, final Integer projectId);

    List<ProjectDTO> getProjectsByOrgId(final Integer orgId, final int pageNo, final int pageSize);
    
    List<ProjectDTO> getProjectsByUserId(final Integer userId, final int pageNo, final int pageSize);

    int count(final Integer orgId);

}
