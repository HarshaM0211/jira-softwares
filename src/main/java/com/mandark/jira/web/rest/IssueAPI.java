package com.mandark.jira.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.service.IssueService;
import com.mandark.jira.spi.web.Responses;


@RestController
@RequestMapping("/api/v1/issues")
public class IssueAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueAPI.class);

    IssueService issueService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody IssueBean issueBean, @RequestParam Integer projectId,
            @RequestParam Integer reporterId) {

        final int issueId = issueService.create(issueBean, projectId, reporterId);

        final String msg = String.format("$API :: Successfully created an Issue with Id : %s", issueId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody IssueBean issueBean, @RequestParam Integer issueId) {

        issueService.update(issueId, issueBean);

        final String msg = String.format("$API :: Successfully Updated an Issue with Id : %s", issueId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

}
