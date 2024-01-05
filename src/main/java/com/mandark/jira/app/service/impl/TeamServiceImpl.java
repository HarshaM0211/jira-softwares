package com.mandark.jira.app.service.impl;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.mandark.jira.app.beans.TeamBean;
import com.mandark.jira.app.dto.TeamDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.app.service.TeamService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class TeamServiceImpl extends AbstractJpaEntityService<Team, TeamBean, TeamDTO> implements TeamService {

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
        return Objects.isNull(entityObj) ? null : new TeamDTO(entityObj);
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
        exEntity.setDescription(entityBean.getDescription());

        return exEntity;
    }

    // Create new Team
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public int create(final Integer orgId, final TeamBean teamBean) {

        // Sanity Checks
        Verify.notNull(orgId, "$create :: orgId must be non NULL");
        Verify.notNull(teamBean, "$create :: teamBean must be non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);

        final Team team = this.createFromBean(teamBean);
        team.setOrganisation(organisation);

        final int teamId = dao.save(team);

        return teamId;
    }

    // Read List of Teams in Org by Org Id
    // ------------------------------------------------------------------------

    @Override
    public List<TeamDTO> getTeamsByOrgId(final Integer orgId, final int pageNo, final int pageSize) {

        // Sanity Checks
        Verify.notNull(orgId, "$getTeamsByOrgId :: orgId must be non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria teamOrgCriteria = Criteria.equal("organisation", organisation);

        final List<Team> teamEntities = dao.find(this.getEntityClass(), teamOrgCriteria, pageNo, pageSize);

        final List<TeamDTO> teamDTOs = super.toDTOs(teamEntities);

        return teamDTOs;
    }

    @Override
    public int count(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId, "$count :: orgId must be non NULL");

        final int count = super.count(this.getOrgCriteria(orgId));

        return count;
    }

    // Criteria
    // ------------------------------------------------------------------------

    public Criteria getOrgCriteria(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId, "$count :: orgId must be non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria criteria = Criteria.equal("organisation", organisation);

        return criteria;
    }
}
