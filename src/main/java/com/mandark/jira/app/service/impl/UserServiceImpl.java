package com.mandark.jira.app.service.impl;

import static com.mandark.jira.app.persistence.orm.entity.User.PROP_EMAIL;
import static com.mandark.jira.app.persistence.orm.entity.User.PROP_ORGANISATION;

import java.util.List;
import java.util.Objects;

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
import com.mandark.jira.spi.lang.ValidationException;
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

        // Sanity Checks
        Verify.notNull(userBean, "$create :: userBean must be non NULL");

        final int userId = super.save(userBean);

        LOGGER.info("Successfully created new User with Id : {}", userId);

        return userId;
    }

    // Add User to Organisation By User Mail
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void addUserToOrgByMail(final Integer orgId, final String userEmail) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(userEmail, "User Mail is NULL");

        final Criteria userEmailCriteria = Criteria.equal(PROP_EMAIL, userEmail);
        final User userEntity = dao.findOne(this.getEntityClass(), userEmailCriteria);
        this.addUserToOrg(orgId, userEntity);

        final String msg = String.format("Successfully added User with mail : %s , into the Organisation with ID : %s",
                userEmail, orgId);
        LOGGER.info(msg);
    }

    // Add User to Organisation By User Entity
    // ------------------------------------------------------------------------

    @Transactional
    public void addUserToOrg(final Integer orgId, final User userEntity) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(userEntity, "UserEntity is NULL");

        if (Objects.nonNull(userEntity.getOrganisation())) {

            final String msg = String.format(
                    "Not Successfull. User with ID :- %s is already a member in the Organisation with ID :- %s",
                    userEntity.getId(), userEntity.getOrganisation().getId());
            throw new ValidationException(msg);
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

        // Sanity Checks
        Verify.notNull(userId, "$update :: userId must be non NULL");
        Verify.notNull(userBean, "$update :: userBean must be non NULL");

        super.update(userId, userBean);
    }

    // Read :: Users in Organisation
    // ------------------------------------------------------------------------

    @Override
    public List<UserDTO> getUsersByOrgId(final Integer orgId, final int pageNo, final int pageSize) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria orgUsersCriteria = Criteria.equal(PROP_ORGANISATION, organisation);
        final List<UserDTO> userDtos = super.find(orgUsersCriteria, pageNo, pageSize);

        LOGGER.info("Successfully fetched Users from Organisation with ID : {}", orgId);

        return userDtos;
    }

    // Count
    // ------------------------------------------------------------------------

    @Override
    public int count(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId, "$count :: Organisation ID : orgId must be non NULL");

        final int count = super.count(this.getOrgCriteria(orgId));

        LOGGER.info("# of entity objects found for critera :: {} - {} : {}", this.getEntityClass(),
                this.getOrgCriteria(orgId), count);
        return count;

    }

    // Criteria
    // ------------------------------------------------------------------------

    @Override
    public Criteria getOrgCriteria(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId, "$getOrgCriteria :: Organisation ID : orgId must be non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria criteria = Criteria.equal(PROP_ORGANISATION, organisation);

        return criteria;
    }

    // Remove User Form Organisation
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public String removeFromOrg(final Integer orgId, final Integer userId) {

        final User user = this.dao.read(this.getEntityClass(), userId, true);

        if (Objects.isNull(user.getOrganisation())) {

            final String msg = String.format("User with Id : %s, is not belongs to any Organisation", userId);
            return msg;

        }
        if (orgId.equals(user.getOrganisation().getId())) {
            user.setOrganisation(null);
            this.dao.update(userId, user);
            final String msg = String.format(
                    "$removeFromOrg :: Successfully removed User with ID : %s , from Organisation with ID : %s", userId,
                    orgId);
            LOGGER.info(msg);
            return msg;
        }
        final String msg = String.format("User with Id : %s, not belongs to Organisation with Id : %s", userId, orgId);

        return msg;
    }

}
