package com.mandark.jira.app.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.UserBean;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.service.UserService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class UserServiceImpl extends AbstractJpaEntityService<User, UserBean, UserDTO> implements UserService {

    // Fields
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    // Constructor
    // ------------------------------------------------------------------------

    public UserServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

    // AbstractJpaEntity Service methods
    // ------------------------------------------------------------------------

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected UserDTO toDTO(User userEntity) {
        return Objects.isNull(userEntity) ? null : new UserDTO(userEntity);
    }

    @Override
    protected User createFromBean(UserBean bean) {
        return copyFromBean(new User(), bean);
    }

    @Override
    protected User copyFromBean(User exEntity, UserBean entityBean) {

        if (Objects.isNull(exEntity) || Objects.isNull(entityBean)) {
            return exEntity;
        }
        exEntity.setFirstName(entityBean.getFirstName());
        exEntity.setLastName(entityBean.getLastName());
        exEntity.setPassword(entityBean.getPassword());
        exEntity.setEmail(entityBean.getEmail());
        exEntity.setPhone(entityBean.getPhone());

        return exEntity;
    }

    // APIs
    // ------------------------------------------------------------------------


    // Create
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public int create(final UserBean userBean) {

        final int userId = super.save(userBean);
        return userId;
    }

    // Add User to Organisation By User Mail
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void addUserToOrgByMail(final Integer orgId, final String userMail) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(userMail, "User Mail is NULL");

        final Criteria userMailCriteria = Criteria.equal("mail", userMail);
        final User userEntity = dao.findOne(this.getEntityClass(), userMailCriteria);
        this.addUserToOrg(orgId, userEntity);
    }

    // Add User to Organisation By User Entity
    // ------------------------------------------------------------------------

    @Transactional
    public void addUserToOrg(final Integer orgId, final User userEntity) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(userEntity, "UserEntity is NULL");

        if (!Objects.isNull(userEntity.getOrganisation())) {
            // TODO throw new Exception ?
            final String msg = String.format(
                    "Not Successfull. User with ID :- %s is already a member in the Organisation with ID :- %s",
                    userEntity.getId(), userEntity.getOrganisation().getId());
            LOGGER.info(msg);
            return;
        }

        final Organisation organisation = dao.read(Organisation.class, orgId, true);

        userEntity.setOrganisation(organisation);

        dao.update(userEntity.getId(), userEntity);

        final String msg =
                String.format("Successfully added User :-ID = %s with mail :- %s to the Organisation with ID :- %s",
                        userEntity.getId(), userEntity.getEmail(), organisation.getId());
        LOGGER.info(msg);

    }

    // Update
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void update(final Integer userId, final UserBean userBean) {
        super.update(userId, userBean);
    }

    // Read :: Users in Organisation
    // ------------------------------------------------------------------------

    @Override
    public List<UserDTO> getUsersByOrgId(final Integer orgId, int pageNo, int pageSize) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria orgUsersCriteria = Criteria.equal("organisation", organisation);
        final List<UserDTO> userDtos = super.find(orgUsersCriteria, null, pageNo, pageSize);

        return userDtos;
    }

    // Delete
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void delete(final Integer userId) {
        super.purge(userId);
    }

}
