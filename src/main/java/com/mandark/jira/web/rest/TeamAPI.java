package com.mandark.jira.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mandark.jira.app.dto.TeamDTO;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.service.TeamService;
import com.mandark.jira.app.service.TeamUserService;
import com.mandark.jira.app.service.UserService;
import com.mandark.jira.spi.web.PageResult;
import com.mandark.jira.spi.web.Pagination;
import com.mandark.jira.spi.web.Responses;
import com.mandark.jira.web.WebConstants;


@RestController
@RequestMapping("/api/v1/orgs/{orgId}/teams")
public class TeamAPI extends AbstractAPI {

    // Fields
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamAPI.class);

    private TeamService teamService;

    private UserService userService;

    private TeamUserService teamUserService;

    // APIs
    // ------------------------------------------------------------------------


    // Team :: Create a new Team
    // ------------------------------------------------------------------------

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestParam String teamName, @PathVariable("orgId") Integer orgId) {

        final int teamId = teamService.create(orgId, teamName);
        final String msg = String.format("Successfully created a Team with ID :- %s", teamId);
        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    // Team :: Read List of Teams in Org by Org Id
    // ------------------------------------------------------------------------

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getTeamsInOrg(@PathVariable("orgId") Integer orgId,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        final List<TeamDTO> teamDTOs = teamService.getTeamsByOrgId(orgId, pageNo, pageSize);

        final int count = teamService.count(orgId);
        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, teamDTOs);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Team :: Add Org User into Team
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{teamId}/users", method = RequestMethod.PUT)
    public ResponseEntity<?> addTeamMember(@RequestParam Integer userId, @PathVariable("teamId") Integer teamId,
            @PathVariable("orgId") Integer orgId) {

        if (userService.isUserInOrg(userId, orgId)) {
            teamUserService.addMember(teamId, userId);
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

    // Team :: Read List of Users in a Team by Team Id
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{teamId}/users", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersByTeamId(@PathVariable("teamId") Integer teamId,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        final List<UserDTO> userDtos = teamUserService.getUsersByTeamId(teamId, pageNo, pageSize);

        final Pagination pagination = Pagination.with(userDtos.size(), pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, userDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Team :: Remove User From the Team
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{teamId}/users", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeTeamMember(@PathVariable("teamId") Integer teamId, @RequestParam Integer userId) {

        teamUserService.removeMember(teamId, userId);

        final String msg =
                String.format("Successfully removed User with Id : %s, from the Team with Id : %s", userId, teamId);

        return Responses.ok(msg);
    }

    // Team :: Read List of Teams a User Included in by User Id
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getTeamsByUserId(@PathVariable("userId") Integer userId,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        final List<TeamDTO> teamDtos = teamUserService.getTeamsByUserId(userId, pageNo, pageSize);

        final Pagination pagination = Pagination.with(teamDtos.size(), pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, teamDtos);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTeamUserService(TeamUserService teamUserService) {
        this.teamUserService = teamUserService;
    }

}
