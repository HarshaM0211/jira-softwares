package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.dto.TeamDTO;
import com.mandark.jira.app.dto.UserDTO;


public interface TeamMemberService {

    List<UserDTO> getUsersByTeamId(final Integer teamId, final int pageNo, final int pageSize);

    void addMember(final Integer teamId, final Integer userId);

    List<TeamDTO> getTeamsByUserId(final Integer userId, final int pageNo, final int pageSize);

    void removeMember(final Integer teamId, final Integer userId);

}
