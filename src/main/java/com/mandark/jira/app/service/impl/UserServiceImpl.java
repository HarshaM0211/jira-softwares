package com.mandark.jira.app.service.impl;

import static com.mandark.jira.app.persistence.orm.entity.Comment.PROP_COMMENTER;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_ASSIGNEE;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_REPORTED_BY;
import static com.mandark.jira.app.persistence.orm.entity.ProjectUser.PROP_USER;
import static com.mandark.jira.app.persistence.orm.entity.User.PROP_EMAIL;
import static com.mandark.jira.app.persistence.orm.entity.User.PROP_ORGANISATION;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.UserBean;
import com.mandark.jira.app.dto.UserDTO;
import com.mandark.jira.app.persistence.orm.entity.Comment;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.ProjectUser;
import com.mandark.jira.app.persistence.orm.entity.Team;
import com.mandark.jira.app.persistence.orm.entity.TeamMember;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.service.UserService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.lang.ValidationException;
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
    protected UserDTO toDTO(User userEntity) {
        return Objects.isNull(userEntity) ? null : new UserDTO(userEntity);
    }

    @Override
    protected User createFromBean(UserBean bean) {
        return copyFromBean(new User(), bean);
    }

    @Override
    protected User copyFromBean(User exEntity, UserBean entityBean) {

        if (Objects.isNull(exEntity) || Objects.isNull(entityBean)) {
            return exEntity;
        }
        exEntity.setFirstName(entityBean.getFirstName());
        exEntity.setLastName(entityBean.getLastName());
        exEntity.setPassword(entityBean.getPassword());
        exEntity.setEmail(entityBean.getEmail());
        exEntity.setPhone(entityBean.getPhone());

        return exEntity;
    }

    // APIs
    // ------------------------------------------------------------------------


    // Create
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public int create(final UserBean userBean) {

        // Sanity Checks
        Verify.notNull(userBean, "$create :: userBean must be non NULL");

        final int userId = super.save(userBean);

        LOGGER.info("Successfully created new User with Id : {}", userId);

        return userId;
    }

    // Add User to Organisation By User Mail
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void addUserToOrgByMail(final Integer orgId, final String userEmail) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(userEmail, "User Mail is NULL");

        final Criteria userEmailCriteria = Criteria.equal(PROP_EMAIL, userEmail);
        final User userEntity = dao.findOne(this.getEntityClass(), userEmailCriteria);
        this.addUserToOrg(orgId, userEntity);

        final String msg = String.format("Successfully added User with mail : %s , into the Organisation with ID : %s",
                userEmail, orgId);
        LOGGER.info(msg);
    }

    // Add User to Organisation By User Entity
    // ------------------------------------------------------------------------

    @Transactional
    public void addUserToOrg(final Integer orgId, final User userEntity) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");
        Verify.notNull(userEntity, "UserEntity is NULL");

        if (Objects.nonNull(userEntity.getOrganisation())) {

            final String msg = String.format(
                    "Not Successfull. User with ID :- %s is already a member in the Organisation with ID :- %s",
                    userEntity.getId(), userEntity.getOrganisation().getId());
            throw new ValidationException(msg);
        }

        final Organisation organisation = dao.read(Organisation.class, orgId, true);

        userEntity.setOrganisation(organisation);

        dao.update(userEntity.getId(), userEntity);

        final String msg =
                String.format("Successfully added User :-ID = %s with mail :- %s to the Organisation with ID :- %s",
                        userEntity.getId(), userEntity.getEmail(), organisation.getId());
        LOGGER.info(msg);

    }

    // Update
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public void update(final Integer userId, final UserBean userBean) {

        // Sanity Checks
        Verify.notNull(userId, "$update :: userId must be non NULL");
        Verify.notNull(userBean, "$update :: userBean must be non NULL");

        super.update(userId, userBean);
    }

    // Read :: Users in Organisation
    // ------------------------------------------------------------------------

    @Override
    public List<UserDTO> getUsersByOrgId(final Integer orgId, final int pageNo, final int pageSize) {
        // Sanity Checks
        Verify.notNull(orgId, "Organisation ID is NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria orgUsersCriteria = Criteria.equal(PROP_ORGANISATION, organisation);
        final List<UserDTO> userDtos = super.find(orgUsersCriteria, pageNo, pageSize);

        LOGGER.info("Successfully fetched Users from Organisation with ID : {}", orgId);

        return userDtos;
    }

    // Count
    // ------------------------------------------------------------------------

    @Override
    public int count(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId, "$count :: Organisation ID : orgId must be non NULL");

        final int count = super.count(this.getOrgCriteria(orgId));

        LOGGER.info("# of entity objects found for critera :: {} - {} : {}", this.getEntityClass(),
                this.getOrgCriteria(orgId), count);
        return count;

    }

    // Criteria
    // ------------------------------------------------------------------------

    @Override
    public Criteria getOrgCriteria(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId, "$getOrgCriteria :: Organisation ID : orgId must be non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria criteria = Criteria.equal(PROP_ORGANISATION, organisation);

        return criteria;
    }

    // Remove User Form Organisation
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public String removeFromOrg(final Integer orgId, final Integer userId) {

        // Sanity Checks
        Verify.notNull(orgId, "$removeFromOrg :: orgId must be non NULL");
        Verify.notNull(userId, "$removeFromOrg :: orgId must be non NULL");

        final User user = this.dao.read(this.getEntityClass(), userId, true);

        if (Objects.isNull(user.getOrganisation())) {

            final String msg = String.format("User with Id : %s, is not belongs to any Organisation", userId);
            return msg;

        }
        if (orgId.equals(user.getOrganisation().getId())) {
            user.setOrganisation(null);

            this.resetComments(user); // Comments will be updated to No-Commenter

            this.resetIssues(user); // Issues will be updated to No-Assignee, No-ReportedBy

            this.resetProjectUser(user); // ManyToMany record of User in Project is deleted

            this.resetTeamMember(user); // ManyToMany record of User in Team is deleted

            this.reserTeamLeader(user); // Teams will be updated to No-TeamLeader

            this.dao.update(userId, user);
            final String msg = String.format(
                    "$removeFromOrg :: Successfully removed User with ID : %s , from Organisation with ID : %s", userId,
                    orgId);
            LOGGER.info(msg);
            return msg;
        }
        final String msg = String.format("User with Id : %s, not belongs to Organisation with Id : %s", userId, orgId);

        return msg;
    }

    private void reserTeamLeader(final User user) {

        // Sanity Checks
        Verify.notNull(user, "$resetIssues :: user must be non NULL");

        final Criteria teamLeadCr = Criteria.equal(Team.PROP_TEAM_LEADER, user);
        final Team team = this.dao.findOne(Team.class, teamLeadCr);
        if (Objects.isNull(team)) {
            return;
        }
        team.setTeamLeader(null);
        this.dao.update(team.getId(), team);
    }

    private void resetTeamMember(final User user) {

        // Sanity Checks
        Verify.notNull(user, "$resetIssues :: user must be non NULL");

        final Criteria userCr = Criteria.equal(TeamMember.PROP_USER, user);
        final int count = this.dao.count(TeamMember.class, userCr);
        final List<TeamMember> teamMembers = this.dao.find(TeamMember.class, userCr, 1, count);
        for (TeamMember teamMem : teamMembers) {
            this.dao.purge(TeamMember.class, teamMem.getId());
        }
    }

    private void resetProjectUser(final User user) {

        // Sanity Checks
        Verify.notNull(user, "$resetIssues :: user must be non NULL");

        final Criteria userCr = Criteria.equal(PROP_USER, user);
        final int count = this.dao.count(ProjectUser.class, userCr);
        final List<ProjectUser> projectsUsers = this.dao.find(ProjectUser.class, userCr, 1, count);

        for (ProjectUser projectUser : projectsUsers) {
            this.dao.purge(ProjectUser.class, projectUser.getId());
        }
    }

    private void resetIssues(final User user) {

        // Sanity Checks
        Verify.notNull(user, "$resetIssues :: user must be non NULL");

        final Criteria assigneeCr = Criteria.equal(PROP_ASSIGNEE, user);
        final Criteria reportedByCr = Criteria.equal(PROP_REPORTED_BY, user);
        final Criteria asgnOrRprtdByCr = Criteria.or(assigneeCr, reportedByCr);
        final int count = this.dao.count(Issue.class, asgnOrRprtdByCr);
        final List<Issue> issues = this.dao.find(Issue.class, asgnOrRprtdByCr, 1, count);

        for (Issue issue : issues) {
            issue.setAssignee(null);
            issue.setReportedBy(null);
            dao.update(issue.getId(), issue);
        }
    }

    private void resetComments(final User user) {

        // Sanity Checks
        Verify.notNull(user, "$resetIssues :: user must be non NULL");

        final Criteria commentsCr = Criteria.equal(PROP_COMMENTER, user);
        final int count = this.dao.count(Comment.class, commentsCr);
        final List<Comment> comments = this.dao.find(Comment.class, commentsCr, 1, count);

        for (Comment cmnt : comments) {
            cmnt.setCommenter(null);
        }
        this.dao.update(comments);
    }

}
