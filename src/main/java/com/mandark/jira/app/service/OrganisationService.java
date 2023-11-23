package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.OrganisationBean;
import com.mandark.jira.app.dto.OrganisationDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.spi.app.service.EntityService;


public interface OrganisationService extends EntityService<Integer, Organisation, OrganisationDTO> {


    int create(final OrganisationBean orgbean);

    OrganisationDTO read(Integer orgId);

    List<OrganisationDTO> read(int pageNo, int pageSize);

    void updateOrganisation(Integer orgId, OrganisationBean orgBean);

}
