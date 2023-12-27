package com.mandark.jira.app.service;

import com.mandark.jira.app.beans.OrganisationBean;
import com.mandark.jira.app.dto.OrganisationDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.spi.app.service.EntityService;


public interface OrganisationService extends EntityService<Integer, Organisation, OrganisationDTO> {


    int create(final OrganisationBean orgbean);

    void update(final Integer orgId, final OrganisationBean orgBean);

    boolean isUserExist(final int userId, final int orgId);

}
