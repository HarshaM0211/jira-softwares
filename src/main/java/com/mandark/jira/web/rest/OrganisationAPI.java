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

import com.mandark.jira.app.beans.OrganisationBean;
import com.mandark.jira.app.dto.OrganisationDTO;
import com.mandark.jira.app.service.OrganisationService;
import com.mandark.jira.spi.web.PageResult;
import com.mandark.jira.spi.web.Pagination;
import com.mandark.jira.spi.web.Responses;
import com.mandark.jira.web.WebConstants;


@RestController
@RequestMapping("/api/v1/orgs")
public class OrganisationAPI extends AbstractAPI {

    // Fields
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationAPI.class);

    private OrganisationService orgService;

    // APIs
    // ------------------------------------------------------------------------

    // Organisation :: Create
    // ------------------------------------------------------------------------

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody OrganisationBean orgBean) {

        final int orgId = orgService.create(orgBean);

        final String msg = String
                .format("Successfully created a new Organisation having id:- %s and added the User as Manager", orgId);
        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    // Organisation :: Read
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrgInfo(@PathVariable("orgId") Integer orgId) {

        final OrganisationDTO orgDto = orgService.read(orgId);

        return Responses.ok(orgDto);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllOrgsInfo(
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_NO,
                    defaultValue = WebConstants.DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = WebConstants.REQ_PARAM_PAGE_SIZE,
                    defaultValue = WebConstants.DEFAULT_PAGE_SIZE) Integer pageSize) {

        final List<OrganisationDTO> organisations = orgService.read(pageNo, pageSize);

        final String msg = String.format("Successfully fetched list of all Organisations");
        LOGGER.info(msg);

        int count = orgService.count();
        Pagination pagination = Pagination.with(count, pageNo, pageSize);
        PageResult pageResult = PageResult.with(pagination, organisations);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    // Organisation :: Update
    // ------------------------------------------------------------------------

    @RequestMapping(value = "/{orgId}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable("orgId") Integer orgId, @RequestBody OrganisationBean orgBean) {

        orgService.update(orgId, orgBean);

        final String msg = String.format("Successfully updated the Organisation with ID :- %s", orgId);

        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setOrgService(OrganisationService orgService) {
        this.orgService = orgService;
    }

}
