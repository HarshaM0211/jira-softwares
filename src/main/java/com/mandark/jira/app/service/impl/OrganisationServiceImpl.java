package com.mandark.jira.app.service.impl;


import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.OrganisationBean;
import com.mandark.jira.app.dto.OrganisationDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.service.OrganisationService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;


public class OrganisationServiceImpl extends AbstractJpaEntityService<Organisation, OrganisationBean, OrganisationDTO>
        implements OrganisationService {


    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationServiceImpl.class);


    public OrganisationServiceImpl(IDao dao) {
        super(dao);
    }

    @Override
    protected Class<Organisation> getEntityClass() {
        return Organisation.class;
    }

    @Override
    protected OrganisationDTO toDTO(Organisation entityObj) {

        if (Objects.isNull(entityObj)) {
            throw new IllegalArgumentException("[failed] - entityObj must not null");
        }

        return new OrganisationDTO(entityObj);
    }

    @Override
    protected Organisation createFromBean(OrganisationBean bean) {
        return copyFromBean(new Organisation(), bean);
    }

    @Override
    protected Organisation copyFromBean(Organisation exEntity, OrganisationBean entityBean) {

        if (Objects.isNull(entityBean)) {
            throw new IllegalArgumentException("[failed] - bean must not null");
        }

        if (Objects.isNull(exEntity)) {
            throw new IllegalArgumentException("[failed] - entity must not null");
        }

        return exEntity;
    }


    // Create
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public int create(OrganisationBean bean) {

        if (Objects.isNull(bean)) {
            throw new IllegalArgumentException("[failed] - bean must not null");
        }
        final int id = this.save(bean);

        return id;
    }

    // Update
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void update(Integer orgId, OrganisationBean orgBean) {

        this.update(orgId, orgBean);
    }

    // Delete
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void delete(Integer orgId) {

        this.purge(orgId);
    }

}
