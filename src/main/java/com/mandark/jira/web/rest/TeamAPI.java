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

import com.mandark.jira.app.beans.TeamBean;
import com.mandark.jira.app.dto.TeamDTO;
import com.mandark.jira.app.service.TeamService;
import com.mandark.jira.app.service.UserService;
import com.mandark.jira.spi.web.PageResult;
import com.mandark.jira.spi.web.Pagination;
import com.mandark.jira.spi.web.Responses;
import com.mandark.jira.web.WebConstants;


@RestController
@RequestMapping("/api/v1/orgs/{orgId}/teams")
public class TeamAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamAPI.class);

    private TeamService teamService;

    private UserService userService;


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody TeamBean teamBean, @PathVariable("orgId") Integer orgId) {

        final int teamId = teamService.create(orgId, teamBean);
        final String msg = String.format("Successfully created a Team with ID :- %s", teamId);
        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getTeamsInOrg(@PathVariable("orgId") Integer orgId,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        List<TeamDTO> teamDTOs = teamService.getTeamsByOrgId(orgId, pageNo, pageSize);

        int count = teamService.count(orgId);
        Pagination pagination = Pagination.with(count, pageNo, pageSize);
        PageResult pageResult = PageResult.with(pagination, teamDTOs);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/{teamId}/users", method = RequestMethod.PUT)
    public ResponseEntity<?> addTeamMember(@RequestParam Integer userId, @PathVariable("teamId") Integer teamId,
            @PathVariable("orgId") Integer orgId) {

        if (userService.isUserInOrg(userId, orgId)) {
            teamService.addMember(userId, teamId);
            final String msg =
                    String.format("Successfully added User with ID :- %s to the Team with ID :- %s", userId, teamId);
            LOGGER.info(msg);
            return Responses.ok(msg);
        }

        final String msg = String.format(
                "UnSuccessfull! First, Add User with ID :- %s to the Organisation with ID :- %s", userId, orgId);
        LOGGER.info(msg);
        return Responses.badRequest(msg);

    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
