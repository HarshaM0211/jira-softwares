package com.mandark.jira.app.service.impl;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.OrganisationBean;
import com.mandark.jira.app.dto.OrganisationDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.service.OrganisationService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class OrganisationServiceImpl extends AbstractJpaEntityService<Organisation, OrganisationBean, OrganisationDTO>
        implements OrganisationService {

    // Fields
    // ------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationServiceImpl.class);

    // Constructor
    // ------------------------------------------------------------------------

    public OrganisationServiceImpl(IDao dao) {
        super(dao);
    }

    // AbstractJpaEntity Service methods
    // ------------------------------------------------------------------------

    @Override
    protected Class<Organisation> getEntityClass() {
        return Organisation.class;
    }

    @Override
    protected OrganisationDTO toDTO(Organisation entityObj) {
        return Objects.isNull(entityObj) ? null : new OrganisationDTO(entityObj);
    }

    @Override
    protected Organisation createFromBean(OrganisationBean bean) {
        return copyFromBean(new Organisation(), bean);
    }

    @Override
    protected Organisation copyFromBean(Organisation exEntity, OrganisationBean entityBean) {

        if (Objects.isNull(exEntity) || Objects.isNull(entityBean)) {
            return exEntity;
        }
        exEntity.setName(entityBean.getName());
        exEntity.setDescription(entityBean.getDescription());

        return exEntity;
    }

    // APIs
    // ------------------------------------------------------------------------


    // Create
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public int create(OrganisationBean bean) {

        // Sanity Checks
        Verify.notNull(bean);

        final Organisation organisation = createFromBean(bean);

        final int id = dao.save(organisation);

        LOGGER.info("#Create :: Organisation is created for the {} with the ID : {}", organisation.getName(), id);

        return id;

    }

    // Read
    // ------------------------------------------------------------------------

    @Override
    public OrganisationDTO read(Integer orgId) {

        Verify.notNull(orgId, "Organisation Id is NULL");

        OrganisationDTO orgDTO = super.read(orgId);
        return orgDTO;
    }

    @Override
    public List<OrganisationDTO> read(int pageNo, int pageSize) {

        List<Organisation> orgEntities = dao.read(Organisation.class, pageNo, pageSize);
        List<OrganisationDTO> orgDtos = orgEntities.stream().map(e -> new OrganisationDTO(e))
                .sorted((e1, e2) -> e1.getId().toString().compareTo(e2.getId().toString()))
                .collect(Collectors.toList());

        return orgDtos;
    }

    // Update
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void updateOrganisation(Integer orgId, OrganisationBean orgBean) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation Id is NULL");
        Verify.notNull(orgBean, "Organisation Bean is NULL");

        this.update(orgId, orgBean);
    }

}
