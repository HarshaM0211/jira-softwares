package com.mandark.jira.app.service;

import com.mandark.jira.app.beans.UserBean;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.service.EntityService;


public interface UserService extends EntityService<Integer, User, UserDTO> {

    int create(final UserBean userBean);

    void addUser(Organisation organisation, User userEntity);

    void updateUser(Integer userId, UserBean userBean);

}
