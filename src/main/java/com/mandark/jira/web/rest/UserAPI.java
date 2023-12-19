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

import com.mandark.jira.app.beans.UserBean;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.service.UserService;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.web.PageResult;
import com.mandark.jira.spi.web.Pagination;
import com.mandark.jira.spi.web.Responses;
import com.mandark.jira.web.WebConstants;


@RestController
@RequestMapping("/api/v1/orgs/{orgId}/users")
public class UserAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationAPI.class);

    private UserService userService;

    // APIs
    // ------------------------------------------------------------------------


    // User :: Create
    // ------------------------------------------------------------------------

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserBean userBean) {

        final int userId = userService.create(userBean);

        final String msg = String.format("Successfully created a new User having id:- %s", userId);
        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    // User :: Update
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("userId") Integer userId, @RequestBody UserBean userBean) {

        userService.update(userId, userBean);

        final String msg = String.format("Successfully Updated the User having id:- %s", userId);
        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    // User :: Add User to an existing Organisation
    // ------------------------------------------------------------------------

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<?> addUser(@PathVariable("orgId") Integer orgId, @RequestParam String userEmail) {

        userService.addUserToOrgByMail(orgId, userEmail);

        final String msg = String.format(
                "Successfully Added the User with email : %s , into the organisation with ID : %s", userEmail, orgId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    // Users :: Read the Users by UserID
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("userId") Integer userId) {

        final UserDTO user = userService.read(userId);

        final String msg = String.format("Successfully fetched User with ID : %s", userId);
        LOGGER.info(msg);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Users :: Read all the Users
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<?> getUsers(
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        final List<UserDTO> users = userService.read(pageNo, pageSize);

        final int count = userService.count();
        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, users);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Users :: Read the Users in the particular Organisation
    // ------------------------------------------------------------------------

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersOfOrg(@PathVariable("orgId") Integer orgId,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        final List<UserDTO> users = userService.getUsersByOrgId(orgId, pageNo, pageSize);

        final int count = userService.count(orgId);
        final Pagination pagination = Pagination.with(count, pageNo, pageSize);
        final PageResult pageResult = PageResult.with(pagination, users);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Users :: Remove from Organisation
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeFromOrg(@PathVariable("userId") Integer userId,
            @PathVariable("orgId") Integer orgId) {

        final String msg = userService.removeFromOrg(orgId, userId);

        LOGGER.info(msg);

        return Responses.ok(msg);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
