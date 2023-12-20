package com.mandark.jira.web.rest;

import static com.mandark.jira.web.WebConstants.DEFAULT_PAGE_NO;
import static com.mandark.jira.web.WebConstants.DEFAULT_PAGE_SIZE;
import static com.mandark.jira.web.WebConstants.REQ_PARAM_PAGE_NO;
import static com.mandark.jira.web.WebConstants.REQ_PARAM_PAGE_SIZE;

import java.util.List;

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

import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.search.bean.IssueSearchQuery;
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

        final int issueId = issueService.create(issueBean, projectId, reporterId);

        final String msg = String.format("$API :: Successfully created an Issue with Id : %s", issueId);
        LOGGER.info(msg);

        return Responses.ok(msg);
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
    public ResponseEntity<?> getIssueById(@PathVariable("issueId") Integer issueId) {

        final IssueDTO issueDto = issueService.getById(issueId);

        return Responses.ok(issueDto);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getIssuesByProjectId(@PathVariable("projectId") Integer projectId,
            @RequestParam(name = REQ_PARAM_PAGE_NO, defaultValue = DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = REQ_PARAM_PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        final int count = issueService.count(projectId);

        final List<IssueDTO> issueDtos = issueService.readAllByProjectId(projectId, pageNo, pageSize);

        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, issueDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/{issueId}/assignee", method = RequestMethod.PUT)
    public ResponseEntity<?> updateIssueAssignee(@PathVariable("issueId") Integer issueId,
            @RequestParam Integer userId) {

        issueService.updateAssignee(issueId, userId);

        final String msg = String.format("$API :: Successfully Updated Assignee of the Issue with Id : %s", issueId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "/{epicId}/childs", method = RequestMethod.PUT)
    public ResponseEntity<?> addExChildIssueToEpic(@PathVariable("epicId") Integer epicId,
            @RequestParam Integer issueId) {

        if (issueService.isEpic(epicId)) {

            issueService.addExChildIssueToEpic(issueId, epicId);

            final String msg = String.format(
                    "$API :: Successfully Added Issue with Id : %s, as child to Epic with Id : %s", issueId, epicId);
            LOGGER.info(msg);

            return Responses.ok(msg);
        }

        final String msg = "Please make sure to select Correct EPIC";
        LOGGER.info(msg);

        return Responses.badRequest(msg);
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
