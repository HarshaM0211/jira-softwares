package com.mandark.jira.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mandark.jira.app.beans.UserBean;
import com.mandark.jira.app.service.UserService;
import com.mandark.jira.spi.web.Responses;


@RestController
@RequestMapping("/api")
public class UserAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationAPI.class);

    private UserService userService;


    // APIs
    // ------------------------------------------------------------------------


    // User :: Create

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserBean userBean) {

        int userId = userService.create(userBean);

        String msg = String.format("Successfully created a new User having id:- %s", userId);
        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    @RequestMapping(value = "/signup/orgs/{orgId}/addUser", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@PathVariable("orgId") Integer orgId) {



        return null;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setUserService(UserService userService) {
        this.userService = userService;
    }



}
