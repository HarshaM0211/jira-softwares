package com.mandark.jira.web.rest;

import static com.mandark.jira.web.WebConstants.DEFAULT_PAGE_NO;
import static com.mandark.jira.web.WebConstants.DEFAULT_PAGE_SIZE;
import static com.mandark.jira.web.WebConstants.REQ_PARAM_PAGE_NO;
import static com.mandark.jira.web.WebConstants.REQ_PARAM_PAGE_SIZE;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mandark.jira.app.bean.search.IssueSearchQuery;
import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.service.IssueService;
import com.mandark.jira.spi.web.PageResult;
import com.mandark.jira.spi.web.Pagination;
import com.mandark.jira.spi.web.Responses;


@RestController
@RequestMapping("/api/v1/projects/{projectId}/issues")
public class IssueAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueAPI.class);

    IssueService issueService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody IssueBean issueBean, @PathVariable("projectId") Integer projectId,
            @RequestParam Integer reporterId) {

        final Integer issueId = issueService.create(issueBean, projectId, reporterId);
        if (Objects.nonNull(issueId)) {

            final String msg = String.format("$API :: Successfully created an Issue with Id : %s", issueId);
            LOGGER.info(msg);

            return Responses.ok(msg);
        }
        final String msg = "$API :: UnSuccessful! Not able to create an Issue. Check Request.";
        LOGGER.info(msg);

        return Responses.badRequest(msg);

    }

    @RequestMapping(value = "/{issueId}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody IssueBean issueBean, @PathVariable("issueId") Integer issueId) {

        issueService.update(issueId, issueBean);

        final String msg = String.format("$API :: Successfully Updated an Issue with Id : %s", issueId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "/{issueId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("issueId") Integer issueId) {

        issueService.purge(issueId);

        final String msg = String.format("$API :: Successfully Deleted the Issue with Id : %s", issueId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "/{issueId}", method = RequestMethod.GET)
    public ResponseEntity<?> getIssueById(@PathVariable("issueId") Integer issueId,
            @PathVariable("projectId") Integer projectId) {

        final IssueDTO issueDto = issueService.getById(issueId, projectId);

        if (Objects.nonNull(issueDto)) {
            return Responses.ok(issueDto);
        }

        final String msg = String.format("Bad Request. Issue with ID : %s not belongs to Project with ID : %s", issueId,
                projectId);
        return Responses.badRequest(msg);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getIssuesByProjectId(@PathVariable("projectId") Integer projectId,
            @RequestParam(name = REQ_PARAM_PAGE_NO, defaultValue = DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = REQ_PARAM_PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        final int count = issueService.count(projectId);

        final List<IssueDTO> issueDtos = issueService.findByProjectId(projectId, pageNo, pageSize);

        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, issueDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/{issueId}/assignee", method = RequestMethod.PUT)
    public ResponseEntity<?> updateIssueAssignee(@PathVariable("issueId") Integer issueId,
            @PathVariable("projectId") Integer projectId, @RequestParam Integer userId) {

        final String msg = issueService.updateAssignee(issueId, userId, projectId);

        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "/{issueId}/assignee", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeIssueAssignee(@PathVariable("issueId") Integer issueId,
            @PathVariable("projectId") Integer projectId) {

        final String msg = issueService.removeAssignee(issueId, projectId);

        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "/{epicId}/childs", method = RequestMethod.PUT)
    public ResponseEntity<?> addExChildIssueToEpic(@PathVariable("epicId") Integer epicId,
            @PathVariable("projectId") Integer projectId, @RequestParam Integer issueId) {

        final String msg = issueService.addExChildIssueToEpic(issueId, epicId, projectId);

        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "/{nonEpicId}/subTasks", method = RequestMethod.PUT)
    public ResponseEntity<?> addSubTaskToNonEpic(@PathVariable("nonEpicId") Integer nonEpicId,
            @PathVariable("projectId") Integer projectId, @RequestParam Integer subTaskId) {

        final String msg = issueService.addSubTaskToNonEpic(subTaskId, nonEpicId, projectId);

        LOGGER.info(msg);

        return Responses.ok(msg);

    }

    @RequestMapping(value = "/nonEpics", method = RequestMethod.GET)
    public ResponseEntity<?> listExChildsForEpic(@PathVariable("projectId") Integer projectId,
            @RequestParam(name = REQ_PARAM_PAGE_NO, defaultValue = DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = REQ_PARAM_PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        final List<IssueDTO> issueDtos = issueService.listValidChildsForEpic(projectId, pageNo, pageSize);
        final int count = issueService.nonEpicCount(projectId);

        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, issueDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/epics", method = RequestMethod.GET)
    public ResponseEntity<?> listExEpicsInProject(@PathVariable("projectId") Integer projectId,
            @RequestParam(name = REQ_PARAM_PAGE_NO, defaultValue = DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = REQ_PARAM_PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        final List<IssueDTO> issueDtos = issueService.listEpicsInProject(projectId, pageNo, pageSize);
        final int count = issueService.count(projectId, Issue.PROP_TYPE, IssueType.EPIC);

        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, issueDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/subTasks", method = RequestMethod.GET)
    public ResponseEntity<?> listExSubTasksInProject(@PathVariable("projectId") Integer projectId,
            @RequestParam(name = REQ_PARAM_PAGE_NO, defaultValue = DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = REQ_PARAM_PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        final List<IssueDTO> issueDtos = issueService.listSubTasks(projectId, pageNo, pageSize);
        final int count = issueService.count(projectId, Issue.PROP_TYPE, IssueType.SUB_TASK);

        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, issueDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> search(@PathVariable("projectId") Integer projectId,
            @RequestParam MultiValueMap<String, String> reqParams,
            @RequestParam(name = REQ_PARAM_PAGE_NO, defaultValue = DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = REQ_PARAM_PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        final IssueSearchQuery isq = new IssueSearchQuery(reqParams);

        final List<IssueDTO> issueDtos = issueService.search(isq, pageNo, pageSize);
        final int count = issueService.count(isq);

        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, issueDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }

}
