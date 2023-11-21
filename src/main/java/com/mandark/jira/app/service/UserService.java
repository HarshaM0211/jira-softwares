package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.UserBean;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.service.EntityService;


public interface UserService extends EntityService<Integer, User, UserDTO> {

    int create(final UserBean userBean);

    void updateUser(Integer userId, UserBean userBean);

    UserDTO read(Integer userId);

    List<UserDTO> getUsers(int pageNo, int pageSize);

    List<UserDTO> getUsers(Integer orgId, int pageNo, int pageSize);

    void delete(Integer userId);

    void addUserToOrgByMail(Integer orgId, String userMail);

    void addUserToOrg(Integer orgId, User userEntity);



}
