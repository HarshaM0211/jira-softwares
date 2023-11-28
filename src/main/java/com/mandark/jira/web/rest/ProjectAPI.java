package com.mandark.jira.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mandark.jira.app.beans.ProjectBean;
import com.mandark.jira.app.dto.ProjectDTO;
import com.mandark.jira.app.service.ProjectService;
import com.mandark.jira.spi.web.PageResult;
import com.mandark.jira.spi.web.Pagination;
import com.mandark.jira.spi.web.Responses;
import com.mandark.jira.web.WebConstants;


@RestController
@RequestMapping("api/v1/orgs/{orgId}/projects")
public class ProjectAPI extends AbstractAPI {

    // Fields
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectAPI.class);

    private ProjectService projectService;

    // APIs
    // ------------------------------------------------------------------------


    // Project :: Create New Project
    // ------------------------------------------------------------------------

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody ProjectBean projectBean, @PathVariable("orgId") Integer orgId) {

        int projectId = projectService.create(orgId, projectBean);

        final String msg = String.format(
                "$API :: Successfully created the Project with Id : %s , in the Organisation with Id : %s", projectId,
                orgId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    // Project :: Read the Projects by OrgId
    // ------------------------------------------------------------------------

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getProjectsByOrgId(@PathVariable("orgId") Integer orgId,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        final int count = projectService.count(orgId);
        final List<ProjectDTO> projetDtos = projectService.getProjectsByOrgId(orgId, pageNo, pageSize);

        Pagination pagination = Pagination.with(count, pageNo, pageSize);
        PageResult pageResult = PageResult.with(pagination, projetDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Project :: Read the Projects by UserId
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getProjectsByUserId(@PathVariable("userId") Integer userId,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        final List<ProjectDTO> projectDtos = projectService.getProjectsByUserId(userId, pageNo, pageSize);

        Pagination pagination = Pagination.with(projectDtos.size(), pageNo, pageSize);
        PageResult pageResult = PageResult.with(pagination, projectDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Project :: Update the Project
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{projectId}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody ProjectBean projectBean,
            @PathVariable("projectId") Integer projectId) {

        projectService.update(projectId, projectBean);

        final String msg = String.format("$API :: Successfully Updated the Project with Id : %s", projectId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    // Project :: Add user
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{projectId}/users", method = RequestMethod.PUT)
    public ResponseEntity<?> addUser(@RequestParam Integer userId, @PathVariable("projectId") Integer projectId) {

        projectService.addUser(userId, projectId);


        final String msg = String.format(
                "$API :: Successfully Added the User with Id : %s, into the Project with Id : %s", userId, projectId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }
    // Project ::



    // Getters and Setters
    // ------------------------------------------------------------------------
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

}