package com.mandark.jira.app.service.impl;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.TeamBean;
import com.mandark.jira.app.dto.TeamDTO;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.service.TeamService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.query.PropertyCriteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class TeamServiceImpl extends AbstractJpaEntityService<Team, TeamBean, TeamDTO> implements TeamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImpl.class);

    // Constructor
    // ------------------------------------------------------------------------
    public TeamServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

    @Override
    protected Class<Team> getEntityClass() {
        return Team.class;
    }

    @Override
    protected TeamDTO toDTO(Team entityObj) {
        return new TeamDTO(entityObj);
    }

    @Override
    protected Team createFromBean(TeamBean bean) {
        return copyFromBean(new Team(), bean);
    }

    @Override
    protected Team copyFromBean(Team exEntity, TeamBean entityBean) {

        if (Objects.isNull(exEntity) || Objects.isNull(entityBean)) {
            return exEntity;
        }
        exEntity.setName(entityBean.getName());
        exEntity.setOrganisation(entityBean.getOrganisation());

        return exEntity;
    }

    @Override
    @Transactional
    public int create(final Integer orgId, TeamBean teamBean) {

        if (Objects.isNull(teamBean.getOrganisation())) {
            Organisation organisation = dao.read(Organisation.class, orgId, false);
            teamBean.setOrganisation(organisation);
        }
        final int teamId = super.save(teamBean);
        return teamId;
    }

    @Override
    @Transactional
    public void addMember(final Integer userId, final Integer teamId) {

        // Sanity Checks
        Verify.notNull(userId, "[failed] :: userId must be non NULL");
        Verify.notNull(teamId, "[failed] :: teamId must be non NULL");

        final User userEntity = dao.read(User.class, userId, false);
        final Team teamEntity = dao.read(Team.class, teamId, false);

        List<User> exUsers = teamEntity.getUsers();
        exUsers.add(userEntity);
        teamEntity.setUsers(exUsers);

        dao.update(teamId, teamEntity);

    }

    @Override
    public List<TeamDTO> getTeamsByOrgId(final Integer orgId, final int pageNo, final int pageSize) {

        // Sanity Checks
        Verify.notNull(orgId, "[failed] orgId must be non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria teamOrgCriteria = Criteria.equal("organisation", organisation);

        List<Team> teamEntities = dao.find(this.getEntityClass(), teamOrgCriteria, pageNo, pageSize);

        List<TeamDTO> teamDTOs = super.toDTOs(teamEntities);

        return teamDTOs;
    }

    @Override
    public int count(final Integer orgId) {

        final int count = super.count(this.getOrgCriteria(orgId));

        return count;
    }

    // Criteria
    // ------------------------------------------------------------------------

    public Criteria getOrgCriteria(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId);

        final Organisation organisation = dao.read(Organisation.class, orgId, false);
        final Criteria criteria = Criteria.equal("organisation", organisation);

        return criteria;
    }

    @Override
    public boolean isUserInOrg(final Integer userId, final Integer orgId) {

        // Sanity Checks
        Verify.notNull(userId);
        Verify.notNull(orgId);

        final User userEntity = dao.read(User.class, userId, true);

        return orgId.equals(userEntity.getOrganisation().getId());
    }

}
