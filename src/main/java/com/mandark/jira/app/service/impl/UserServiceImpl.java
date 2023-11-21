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
    protected UserDTO toDTO(User entityObj) {
        return new UserDTO(entityObj);
    }

    @Override
    protected User createFromBean(UserBean bean) {
        return copyFromBean(new User(), bean);
    }

    @Override
    protected User copyFromBean(User exEntity, UserBean entityBean) {

        if (!Objects.isNull(entityBean.getUserName())) {
            exEntity.setUserName(entityBean.getUserName());
        }

        if (!Objects.isNull(entityBean.getPassword())) {
            exEntity.setPassword(entityBean.getPassword());
        }

        if (!Objects.isNull(entityBean.getMail())) {
            exEntity.setMail(entityBean.getMail());
        }

        if (!Objects.isNull(entityBean.getRole())) {
            exEntity.setRole(entityBean.getRole());
        }

        return exEntity;
    }

    // APIs
    // ------------------------------------------------------------------------


    // Create
    // ------------------------------------------------------------------------


    @Override
    @Transactional
    public int create(UserBean userBean) {
        // Sanity Checks
        Verify.notNull(userBean, "User Bean is NULL");

        User userEntity = new User();
        this.copyFromBean(userEntity, userBean);

        int userId = dao.save(userEntity);

        return userId;
    }

    // Add User to Organisation By User Mail
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void addUserToOrgByMail(Integer orgId, String userMail) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(userMail, "User Mail is NULL");

        Criteria userMailCriteria = Criteria.equal("mail", userMail);
        User user = dao.findOne(this.getEntityClass(), userMailCriteria);
        this.addUserToOrg(orgId, user);

    }

    // Add User to Organisation By User Entity
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void addUserToOrg(Integer orgId, User userEntity) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(userEntity, "UserEntity is NULL");

        if (!Objects.isNull(userEntity.getOrganisation())) {
            // throw new Exception
            String msg = String.format(
                    "Not Successfull. User with ID :- %s is already a member in the Organisation with ID :- %s",
                    userEntity.getId(), userEntity.getOrganisation().getId());
            LOGGER.info(msg);
            return;
        }

        Organisation organisation = dao.read(Organisation.class, orgId, true);
        // if (Objects.isNull(userEntity.getOrganisation())) {

        userEntity.setOrganisation(organisation);

        dao.update(userEntity.getId(), userEntity);

        String msg =
                String.format("Successfully added User :-ID = %s with mail :- %s to the Organisation with ID :- %s",
                        userEntity.getId(), userEntity.getMail(), organisation.getId());
        LOGGER.info(msg);

    }

    // Update
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void updateUser(Integer userId, UserBean userBean) {
        // Sanity Checks
        Verify.notNull(userId, "UserId is NULL");
        Verify.notNull(userBean, "UserBean is NULL");

        User userEntity = new User();
        this.copyFromBean(userEntity, userBean);

        dao.update(userId, userEntity);

    }

    // Read :: All Users
    // ------------------------------------------------------------------------

    @Override
    public List<UserDTO> getUsers(int pageNo, int pageSize) {
        // Sanity Checks
        Verify.notNull(pageNo, "PageNo is NULL");
        Verify.notNull(pageSize, "PageSize is NULL");

        List<User> userEntities = dao.read(this.getEntityClass(), pageNo, pageSize);
        List<UserDTO> userDtos = userEntities.stream().map(e -> new UserDTO(e))
                .sorted((e1, e2) -> e1.getId().toString().compareTo(e2.getId().toString()))
                .collect(Collectors.toList());

        return userDtos;
    }

    // Read :: Users in Organisation
    // ------------------------------------------------------------------------

    @Override
    public List<UserDTO> getUsers(Integer orgId, int pageNo, int pageSize) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(pageNo, "PageNo is NULL");
        Verify.notNull(pageSize, "PageSize is NULL");

        Organisation organisation = dao.read(Organisation.class, orgId, true);

        Criteria orgUsersCriteria = Criteria.equal("organisation", organisation);

        List<User> userEntities = dao.find(this.getEntityClass(), orgUsersCriteria, pageNo, pageSize);
        List<UserDTO> userDtos = userEntities.stream().map(e -> new UserDTO(e))
                .sorted((e1, e2) -> e1.getId().toString().compareTo(e2.getId().toString()))
                .collect(Collectors.toList());

        return userDtos;
    }

    // Delete
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void delete(Integer userId) {
        // Sanity Checks
        Verify.notNull(userId, "User ID is NULL");

        dao.purge(this.getEntityClass(), userId);


    }

}
