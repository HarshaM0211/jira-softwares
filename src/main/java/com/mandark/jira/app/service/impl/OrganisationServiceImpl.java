package com.mandark.jira.app.service.impl;


import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.OrganisationBean;
import com.mandark.jira.app.dto.OrganisationDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.User;
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

    public OrganisationServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

    // Super Class implementations
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
    public int create(final OrganisationBean bean) {

        // Sanity Checks
        Verify.notNull(bean);

        final int id = super.save(bean);

        return id;
    }

    // Update
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void update(final Integer orgId, final OrganisationBean orgBean) {

        // Sanity Checks
        Verify.notNull(orgId, "Organisation Id is NULL");
        Verify.notNull(orgBean, "Organisation Bean is NULL");

        super.update(orgId, orgBean);
    }

    @Override
    public boolean isUserExist(final int userId, final int orgId) {

        // Sanity Checks
        Verify.notNull(userId, "$isUserInOrg :: userId must be non NULL");
        Verify.notNull(orgId, "$isUserInOrg :: orgId must be non NULL");

        final User userEntity = this.dao.read(User.class, userId, true);

        if (Objects.isNull(userEntity.getOrganisation())) {

            final String msg = String.format("User doesn't exist in any of the Organisation");
            LOGGER.info(msg);
            return false;
        }
        final boolean isExist = orgId == userEntity.getOrganisation().getId();
        final String msg = String.format("Status : User Existence in the Organisation : %s", isExist);
        LOGGER.info(msg);

        return isExist;
    }

}
