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
import org.springframework.web.bind.annotation.RestController;

import com.mandark.jira.app.beans.OrganisationBean;
import com.mandark.jira.app.dto.OrganisationDTO;
import com.mandark.jira.app.service.OrganisationService;
import com.mandark.jira.spi.web.Responses;
import com.mandark.jira.web.WebConstants;


@RestController
@RequestMapping("/api/orgs")
public class OrganisationAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationAPI.class);

    private OrganisationService orgService;



    // APIs
    // ------------------------------------------------------------------------

    // Organisation :: Create

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createOrganisation(@RequestBody OrganisationBean orgBean) {

        int orgId = orgService.create(orgBean);

        String msg = String
                .format("Successfully created a new Organisation having id:- %s and added the User as Manager", orgId);
        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    // Organisation :: Read

    @RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
    public ResponseEntity<?> readOrganisation(@PathVariable("orgId") Integer orgId) {

        OrganisationDTO orgDto = orgService.read(orgId);

        return Responses.ok(orgDto);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> readOrgs() {

        int pageNo = Integer.parseInt(WebConstants.DEFAULT_PAGE_NO);
        int pageSize = Integer.parseInt(WebConstants.DEFAULT_PAGE_SIZE);

        List<OrganisationDTO> organisations = orgService.read(pageNo, pageSize);

        return new ResponseEntity<>(organisations, HttpStatus.OK);
    }


    // Organisation :: Update

    @RequestMapping(value = "/{orgId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrganisation(@PathVariable("orgId") Integer orgId,
            @RequestBody OrganisationBean orgBean) {

        orgService.updateOrganisation(orgId, orgBean);

        String msg = String.format("Successfully updated the Organisation with ID :- %s", orgId);

        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    // Organisation :: Delete

    @RequestMapping(value = "/{orgId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrg(@PathVariable("orgId") Integer orgId) {

        orgService.deleteOrganisation(orgId);

        String msg = String.format("Successfully deleted the Organisation with ID :- %s", orgId);

        LOGGER.info(msg);
        return Responses.ok(msg);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setOrgService(OrganisationService orgService) {
        this.orgService = orgService;
    }

}