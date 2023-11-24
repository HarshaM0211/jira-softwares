package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.TeamBean;
import com.mandark.jira.app.dto.TeamDTO;
import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.EntityService;


public interface TeamService extends EntityService<Integer, Team, TeamDTO> {

    int create(Integer orgId, TeamBean teamBean);

    void addMember(Integer userId, Integer teamId);

    List<TeamDTO> getTeamsByOrgId(final Integer orgId, final int pageNo, final int pageSize);
    
    int count(Integer orgId);
    
}
