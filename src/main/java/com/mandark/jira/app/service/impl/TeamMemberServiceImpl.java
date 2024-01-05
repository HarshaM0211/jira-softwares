package com.mandark.jira.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.dto.TeamDTO;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.app.persistence.orm.entity.TeamMember;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.service.TeamMemberService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.util.Verify;


public class TeamMemberServiceImpl implements TeamMemberService {

    // Fields
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImpl.class);

    private IDao<Integer> dao;

    // Service Methods
    // ------------------------------------------------------------------------

    protected Class<TeamMember> getEntityClass() {
        return TeamMember.class;
    }

    // Add User as a Team Member
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void addMember(final Integer teamId, final Integer userId) {

        // Sanity Checks
        Verify.notNull(userId, "[failed] :: userId must be non NULL");
        Verify.notNull(teamId, "[failed] :: teamId must be non NULL");

        final TeamMember teamMembers = dao.findOne(TeamMember.class, this.getTeamAndUserCriteria(teamId, userId));

        if (!Objects.isNull(teamMembers)) {
            LOGGER.info("User with Id : %s, already Exists in the specified Team with Id : %s", userId, teamId);
            return;
        }

        final User userEntity = dao.read(User.class, userId, true);
        final Team teamEntity = dao.read(Team.class, teamId, true);

        final TeamMember teamMember = new TeamMember();
        teamMember.setTeam(teamEntity);
        teamMember.setUser(userEntity);
        dao.save(teamMember);


        final String msg =
                String.format("Successfully Added User with Id : %s, to the Team with Id : %s", userId, teamId);
        LOGGER.info(msg);
    }

    // Read Users in Team by Team Id
    // ------------------------------------------------------------------------

    @Override
    public List<UserDTO> getUsersByTeamId(final Integer teamId, final int pageNo, final int pageSize) {

        // Sanity checks
        Verify.notNull(teamId, "$getUsersBtTeamId :: teamId must be non NULL");

        final Team teamEntity = dao.read(Team.class, teamId, true);
        final Criteria teamcriteria = Criteria.equal("team", teamEntity);
        final List<TeamMember> teamMembers = dao.find(TeamMember.class, teamcriteria, pageNo, pageSize);

        if (Objects.isNull(teamMembers)) {

            final String msg = String.format("Cannot find the Users having Team with Id : %s", teamId);
            LOGGER.info(msg);

            throw new IllegalArgumentException(msg);
        }

        final List<UserDTO> userDtos = new ArrayList<>();
        for (TeamMember tu : teamMembers) {
            final User user = tu.getUser();
            final UserDTO userDto = new UserDTO(user);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    // Read Teams a User included in by User Id
    // ------------------------------------------------------------------------

    @Override
    public List<TeamDTO> getTeamsByUserId(final Integer userId, int pageNo, int pageSize) {

        // Sanity checks
        Verify.notNull(userId);

        final User userEntity = dao.read(User.class, userId, true);
        final Criteria usercriteria = Criteria.equal("user", userEntity);
        final List<TeamMember> teamMembers = dao.find(TeamMember.class, usercriteria, pageNo, pageSize);

        if (Objects.isNull(teamMembers)) {

            final String msg = String.format("Cannot find the Teams having User with Id : %s", userId);
            LOGGER.info(msg);

            throw new IllegalArgumentException(msg);
        }

        final List<TeamDTO> teamDtos = new ArrayList<>();
        for (TeamMember tu : teamMembers) {
            final Team team = tu.getTeam();
            final TeamDTO teamDto = new TeamDTO(team);
            teamDtos.add(teamDto);
        }
        return teamDtos;
    }

    // Remove a User from Team
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public String removeMember(final Integer teamId, final Integer userId) {

        // Sanity Checks
        Verify.notNull(teamId, "$removeMember :: teamId must be non NULL");
        Verify.notNull(userId, "$removeMember :: userId must be non NULL");

        final Criteria teamAndUserCriteria = this.getTeamAndUserCriteria(teamId, userId);
        final TeamMember teamMemberEntity = dao.findOne(this.getEntityClass(), teamAndUserCriteria);

        if (Objects.isNull(teamMemberEntity)) {

            final String msg = String.format("UnSuccessful! Cannot find User with Id : %s, in the Team with Id : %s",
                    userId, teamId);
            LOGGER.info(msg);

            return msg;
        }
        dao.purge(this.getEntityClass(), teamMemberEntity.getId());

        final String msg =
                String.format("Successfully removed User with Id : %s, from the Team with Id : %s", userId, teamId);
        LOGGER.info(msg);
        return msg;
    }

    // Criteria
    // ------------------------------------------------------------------------

    public Criteria getTeamAndUserCriteria(final Integer teamId, final Integer userId) {

        final User userEntity = dao.read(User.class, userId, true);
        final Team teamEntity = dao.read(Team.class, teamId, true);

        final Criteria teamcriteria = Criteria.equal("team", teamEntity);
        final Criteria userCriteria = Criteria.equal("user", userEntity);

        final List<Criteria> criteriaList = new ArrayList<Criteria>();
        criteriaList.add(teamcriteria);
        criteriaList.add(userCriteria);

        final Criteria teamAndUserCriteria = Criteria.and(criteriaList);

        return teamAndUserCriteria;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setDao(IDao<Integer> dao) {
        this.dao = dao;
    }
}
