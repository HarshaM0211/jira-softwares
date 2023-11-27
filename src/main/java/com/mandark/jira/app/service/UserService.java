package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.UserBean;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.EntityService;


public interface UserService extends EntityService<Integer, User, UserDTO> {

    int create(final UserBean userBean);

    void update(final Integer userId, final UserBean userBean);

    List<UserDTO> getUsersByOrgId(final Integer orgId, final int pageNo, final int pageSize);

    void removeFromOrg(final Integer orgId, final Integer userId);

    void addUserToOrgByMail(final Integer orgId, final String userMail);

    int count(final Integer orgId);

    Criteria getOrgCriteria(final Integer orgId);
}
