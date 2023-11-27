package com.mandark.jira.app.service.impl;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.ProjectBean;
import com.mandark.jira.app.dto.ProjectDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.service.ProjectService;
import com.mandark.jira.app.service.UserService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class ProjectServiceImpl extends AbstractJpaEntityService<Project, ProjectBean, ProjectDTO>
        implements ProjectService {

    // Fields
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationServiceImpl.class);

    private UserService userService;

    // Constructor
    // ------------------------------------------------------------------------

    public ProjectServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }

    @Override
    protected ProjectDTO toDTO(Project entityObj) {
        return Objects.isNull(entityObj) ? null : new ProjectDTO(entityObj);
    }

    @Override
    protected Project createFromBean(ProjectBean bean) {
        return this.copyFromBean(new Project(), bean);
    }

    @Override
    protected Project copyFromBean(Project exEntity, ProjectBean entityBean) {

        if (Objects.isNull(exEntity) || Objects.isNull(entityBean)) {
            return exEntity;
        }
        exEntity.setName(entityBean.getName());
        exEntity.setProjectKey(entityBean.getProjectKey());
        exEntity.setDescription(entityBean.getDescription());

        return exEntity;
    }

    @Override
    @Transactional
    public int create(final Integer orgId, ProjectBean entityBean) {

        // Sanity Checks
        Verify.notNull(entityBean, "$create :: entityBean must be Non NULL");
        Verify.notNull(orgId, "$create :: orgId must be Non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);

        Project project = this.createFromBean(entityBean);
        project.setOrganisation(organisation);

        final int projectId = dao.save(project);
        final String msg = String.format("$ServiceImpl :: Successfully created the Project with ID : %s", projectId);
        LOGGER.info(msg);

        return projectId;
    }

    @Override
    @Transactional
    public void update(final Integer projectId, ProjectBean projectBean) {

        // Sanity Checks
        Verify.notNull(projectId, "$update :: projectId must be non NULL");
        Verify.notNull(projectBean, "$update :: projectBean must be non NULL");

        super.update(projectId, projectBean);
    }

    @Override
    @Transactional
    public void addUser(final Integer userId, final Integer projectId) {

        // Sanity Checks
        Verify.notNull(userId, "$addUser :: userId must be non NULL");
        Verify.notNull(projectId, "$addUser :: projectId must be non NULL");

        Project project = dao.read(this.getEntityClass(), projectId, true);
        final User user = dao.read(User.class, userId, true);

        List<User> exUsers = project.getUsers();
        exUsers.add(user);

        project.setUsers(exUsers);
        dao.update(projectId, project);

        final String msg =
                String.format("$ServiceImpl :: Successfully added User with Id : %s , into the project with Id : %s",
                        userId, projectId);
        LOGGER.info(msg);
    }

    @Override
    public List<ProjectDTO> getProjectsByOrgId(final Integer orgId, final int pageNo, final int pageSize) {

        // Sanity Checks
        Verify.notNull(orgId);

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria projOrgCriteria = Criteria.equal("organisation", organisation);

        List<Project> projects = dao.find(this.getEntityClass(), projOrgCriteria, pageNo, pageSize);

        final List<ProjectDTO> projectDTOs = super.toDTOs(projects);
        return projectDTOs;
    }

    @Override
    public List<ProjectDTO> getProjectsByUserId(final Integer userId, final int pageNo, final int pageSize) {

        // Sanity Checks
        Verify.notNull(userId);

        final User user = dao.read(User.class, userId, true);
        List<Project> projects = user.getProjects();

        final List<ProjectDTO> projectDTOs = super.toDTOs(projects);
        return projectDTOs;
    }

    @Override
    public int count(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId, "$count :: orgId must be non NULL");

        final int count = super.count(userService.getOrgCriteria(orgId));
        return count;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
