package com.mandark.jira.web.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.mandark.jira.app.beans.SprintBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.dto.SprintDTO;
import com.mandark.jira.app.service.SprintService;
import com.mandark.jira.spi.web.Responses;


@RestController
@RequestMapping(value = "api/v1/projects/{projectId}/sprints")
public class SprintAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintAPI.class);

    SprintService sprintService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable("projectId") int projectId) {

        final int sprintId = sprintService.create(projectId);

        final String msg = String.format("$API :: Successfully created the Sprint with Id : %s", sprintId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getByProjectId(@PathVariable("projectId") int projectId) {

        final List<SprintDTO> sprintDtos = sprintService.getByProjectId(projectId);

        return new ResponseEntity<>(sprintDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{sprintId}", method = RequestMethod.GET)
    public ResponseEntity<?> getIssues(@PathVariable("sprintId") int sprintId) {

        final List<IssueDTO> issueDtos = sprintService.getIssues(sprintId);

        return new ResponseEntity<>(issueDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{sprintId}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody SprintBean sprintBean, @PathVariable("sprintId") int sprintId) {

        sprintService.update(sprintId, sprintBean);

        final String msg = String.format("$API :: Successfully updated the Sprint with Id : %s", sprintId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "/{sprintId}/{status}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStatus(@PathVariable("sprintId") int sprintId,
            @RequestParam(required = false) Integer nextSprintId, @PathVariable("status") String status) {

        if (status.equals("start")) {

            sprintService.start(sprintId);

            final String msg = String.format("$API :: Successfully Started the Sprint with Id : %s", sprintId);
            LOGGER.info(msg);

            return Responses.ok(msg);
        }
        if (status.equals("complete")) {

            final String msg = sprintService.complete(sprintId, nextSprintId);

            LOGGER.info(msg);

            return Responses.ok(msg);
        }
        throw new IllegalArgumentException("Bad Request. No Service available for the request");
    }

    @RequestMapping(value = "/{sprintId}/issues/{issueId}", method = RequestMethod.PUT)
    public ResponseEntity<?> removeIssue(@PathVariable int issueId) {

        final String msg = sprintService.removeIssue(issueId);

        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "/{sprintId}/issues", method = RequestMethod.PUT)
    public ResponseEntity<?> addIssues(@RequestBody Map<String, int[]> issueIds,
            @PathVariable("sprintId") int sprintId) {

        int[] idsArray = issueIds.get("issueIds");
        final List<Integer> issueIdList = Arrays.stream(idsArray).boxed().collect(Collectors.toList());
        sprintService.addIssues(issueIdList, sprintId);

        final String msg =
                String.format("$API :: Added NonEpic and NonSubTask Issues to Sprint with Id : %s", sprintId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setSprintService(SprintService sprintService) {
        this.sprintService = sprintService;
    }
}
