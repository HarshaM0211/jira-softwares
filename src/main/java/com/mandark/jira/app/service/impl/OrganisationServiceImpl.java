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
import com.mandark.jira.spi.app.persistence.IEntity;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;


public class OrganisationServiceImpl extends AbstractJpaEntityService<Organisation, OrganisationBean, OrganisationDTO>
        implements OrganisationService {


    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationServiceImpl.class);


    public OrganisationServiceImpl(IDao dao) {
        super(dao);
    }

    @Override
    protected Class getEntityClass() {
        return Organisation.class;
    }

    @Override
    protected OrganisationDTO toDTO(Organisation entityObj) {
        return new OrganisationDTO(entityObj);
    }

    @Override
    protected Organisation createFromBean(OrganisationBean bean) {
        return copyFromBean(new Organisation(), bean);
    }

    @Override
    protected Organisation copyFromBean(Organisation exEntity, OrganisationBean entityBean) {

        if (Objects.nonNull(entityBean.getName())) {
            exEntity.setName(entityBean.getName());
        }

        if (Objects.nonNull(entityBean.getDescription())) {
            exEntity.setDescription(entityBean.getDescription());
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
        Organisation organisation = createFromBean(bean);

        int id = dao.save(organisation);

        LOGGER.info("#Create :: Organisation is created for the {} with the ID : {}", organisation.getName(), id);

        return id;

    }

    // Read
    // ------------------------------------------------------------------------

    @Override
    public OrganisationDTO read(Integer id) {

        OrganisationDTO orgDTO = super.read(id);
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

        super.update(orgId, orgBean);
    }

    // Delete
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void deleteOrganisation(Integer orgId) {

        super.purge(orgId);
    }

}
