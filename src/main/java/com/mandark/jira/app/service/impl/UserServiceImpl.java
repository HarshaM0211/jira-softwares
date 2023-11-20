package com.mandark.jira.app.service.impl;

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
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class UserServiceImpl extends AbstractJpaEntityService<User, UserBean, UserDTO> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

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

        if (Objects.isNull(entityBean.getUserName())) {
            exEntity.setUserName(entityBean.getUserName());
        }

        if (Objects.isNull(entityBean.getPassword())) {
            exEntity.setPassword(entityBean.getPassword());
        }

        if (Objects.isNull(entityBean.getMail())) {
            exEntity.setMail(entityBean.getMail());
        }

        if (Objects.isNull(entityBean.getRole())) {
            exEntity.setRole(entityBean.getRole());
        }

        return exEntity;
    }

    @Override
    @Transactional
    public int create(UserBean userBean) {

        Verify.notNull(userBean, "User Bean is NULL");

        User userEntity = new User();
        this.copyFromBean(userEntity, userBean);

        int userId = dao.save(userEntity);

        return userId;
    }

    @Override
    @Transactional
    public void addUser(Organisation organisation, User userEntity) {

        Verify.notNull(organisation, "Organisation Entity is NULL");
        Verify.notNull(userEntity, "UserEntity is NULL");

        if (!Objects.isNull(userEntity.getOrganisation())) {
            // throw new Exception
            String msg = String.format(
                    "Not Successfull. User with ID :- %s is already a member in the Organisation with ID :- %s",
                    userEntity.getId(), userEntity.getOrganisation().getId());
            LOGGER.info(msg);
            return;
        }

        // if (Objects.isNull(userEntity.getOrganisation())) {

        userEntity.setOrganisation(organisation);
        int userId = dao.save(userEntity);

        String msg = String.format("Successfully added User with ID :- %s to the Organisation with ID :- %s", userId,
                organisation.getId());
        LOGGER.info(msg);

    }

    @Override
    @Transactional
    public void updateUser(Integer userId, UserBean userBean) {

        Verify.notNull(userId, "UserId is NULL");
        Verify.notNull(userBean, "UserBean is NULL");

        User userEntity = new User();
        this.copyFromBean(userEntity, userBean);

        dao.update(userId, userEntity);

        String msg = String.format("Successfully updated the details of User with Id :- %s", userId);
        LOGGER.info(msg);

    }

}
