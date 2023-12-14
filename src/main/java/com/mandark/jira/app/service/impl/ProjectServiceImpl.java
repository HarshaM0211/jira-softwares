package com.mandark.jira.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.ProjectBean;
import com.mandark.jira.app.dto.ProjectDTO;
import com.mandark.jira.app.persistence.orm.entity.Organisation;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.ProjectUser;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.service.ProjectService;
import com.mandark.jira.app.service.UserService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;
import com.mandark.jira.web.WebConstants;


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
    public int create(final Integer orgId, final ProjectBean entityBean) {

        // Sanity Checks
        Verify.notNull(entityBean, "$create :: entityBean must be Non NULL");
        Verify.notNull(orgId, "$create :: orgId must be Non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);

        Project project = new Project();
        if (Objects.nonNull(entityBean.getProjectKey())) {
            project = this.createFromBean(entityBean);
        }
        if (Objects.isNull(entityBean.getProjectKey())) {

            List<String> keyList = this.generateKeys(orgId, entityBean.getName());
            for (String key : keyList) {
                int projectsCount = this.count(orgId);
                int pageNo = Integer.parseInt(WebConstants.DEFAULT_PAGE_NO);
                if (this.isKeyUnique(key, orgId, pageNo, projectsCount)) {
                    LOGGER.info("Key :{} is unique for given Project Name", key);
                    project.setProjectKey(key);
                    break;
                }
            }
            project.setName(entityBean.getName());
            project.setDescription(entityBean.getDescription());
        }
        project.setOrganisation(organisation);

        final int projectId = dao.save(project);
        final String msg = String.format("$ServiceImpl :: Successfully created the Project with ID : %s", projectId);
        LOGGER.info(msg);

        return projectId;
    }

    @Override
    public List<ProjectDTO> findByOrgIdAndKey(final Integer orgId, final String projectKey, final int pageNo,
            final int pageSize) {

        // Sanity Checks
        Verify.notNull(orgId, "$findByOrgIdAndKey :: orgId must be non NULL");
        Verify.notNull(projectKey, "$findByOrgIdAndKey :: projectKey must be non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);

        final Criteria orgCriteria = Criteria.equal(Project.PROP_ORGANISATION, organisation);
        final Criteria keyCriteria = Criteria.equal(Project.PROP_PROJECT_KEY, projectKey);

        final List<Criteria> criteriaList = new ArrayList<Criteria>();
        criteriaList.add(orgCriteria);
        criteriaList.add(keyCriteria);

        final Criteria orgAndKeyCriteria = Criteria.and(criteriaList);

        final List<Project> projects = this.dao.find(this.getEntityClass(), orgAndKeyCriteria, pageNo, pageSize);
        final List<ProjectDTO> projectDtos = super.toDTOs(projects);

        LOGGER.info("Projects found with given OrgId and Key: {}", projectDtos);
        return projectDtos;
    }

    @Override
    @Transactional
    public void update(final Integer projectId, final ProjectBean projectBean) {

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

        final ProjectUser projectUser = dao.findOne(ProjectUser.class, this.getProjectUsersCriteria(projectId, userId));

        if (Objects.nonNull(projectUser)) {
            LOGGER.info("User with Id : %s, already Exists in the specified Project with Id : %s", userId, projectId);
            return;
        }

        final User userEntity = dao.read(User.class, userId, true);
        final Project projectEntity = dao.read(Project.class, projectId, true);

        final ProjectUser projectUserEntity = new ProjectUser();
        projectUserEntity.setProject(projectEntity);
        projectUserEntity.setUser(userEntity);
        dao.save(projectUserEntity);

        final String msg =
                String.format("Successfully Added User with Id : %s, to the Project with Id : %s", userId, projectId);
        LOGGER.info(msg);
    }

    @Override
    public List<ProjectDTO> findByOrgId(final Integer orgId, final int pageNo, final int pageSize) {

        // Sanity Checks
        Verify.notNull(orgId);

        final Organisation organisation = dao.read(Organisation.class, orgId, true);
        final Criteria projOrgCriteria = Criteria.equal(Project.PROP_ORGANISATION, organisation);

        final List<Project> projects = dao.find(this.getEntityClass(), projOrgCriteria, pageNo, pageSize);

        final List<ProjectDTO> projectDTOs = super.toDTOs(projects);
        return projectDTOs;
    }

    @Override
    public List<ProjectDTO> findByUserId(final Integer userId, final int pageNo, final int pageSize) {

        // Sanity Checks
        Verify.notNull(userId, "$getProjectsByUserId :: userId must be non NULL");

        final User user = dao.read(User.class, userId, true);
        final Criteria criteria = Criteria.equal(ProjectUser.PROP_USER, user);

        final List<ProjectUser> userProjects = dao.find(ProjectUser.class, criteria, pageNo, pageSize);

        final List<ProjectDTO> projectDtos = new ArrayList<ProjectDTO>();
        for (ProjectUser pu : userProjects) {
            final Project project = pu.getProject();
            final ProjectDTO projectDto = this.toDTO(project);
            projectDtos.add(projectDto);
        }
        return projectDtos;
    }

    @Override
    @Transactional
    public void removeUser(final Integer projectId, final Integer userId) {

        // Sanity Checks
        Verify.notNull(projectId, "$removeUser :: projectId must be non NULL");
        Verify.notNull(userId, "$removeUser :: userId must be non NULL");

        final ProjectUser projectUser = dao.findOne(ProjectUser.class, this.getProjectUsersCriteria(projectId, userId));

        if (Objects.isNull(projectUser)) {

            final String msg = String.format(
                    "UnSuccessfull! Cannot find User with Id : %s, in the Project with Id : %s", userId, projectId);
            LOGGER.info(msg);

            return;
        }
        dao.purge(ProjectUser.class, projectUser.getId());

        final String msg = String.format("Successfully removed User with Id : %s, from the Project with Id : %s",
                userId, projectId);
        LOGGER.info(msg);

    }

    @Override
    public int count(final Integer orgId) {

        // Sanity Checks
        Verify.notNull(orgId, "$count :: orgId must be non NULL");

        final int count = super.count(userService.getOrgCriteria(orgId));
        return count;
    }

    // Criteria
    // ------------------------------------------------------------------------

    public Criteria getProjectUsersCriteria(final Integer projectId, final Integer userId) {

        // Sanity Checks
        Verify.notNull(projectId, "$getProjectUsersCriteria :: projectId must be non NULL");
        Verify.notNull(userId, "$getProjectUsersCriteria :: userId must be non NULL");

        final User userEntity = dao.read(User.class, userId, true);
        final Project projectEntity = dao.read(Project.class, projectId, true);

        final Criteria projectcriteria = Criteria.equal(ProjectUser.PROP_PROJECT, projectEntity);
        final Criteria userCriteria = Criteria.equal(ProjectUser.PROP_USER, userEntity);

        final List<Criteria> criteriaList = new ArrayList<Criteria>();
        criteriaList.add(projectcriteria);
        criteriaList.add(userCriteria);

        final Criteria projectAndUserCriteria = Criteria.and(criteriaList);

        return projectAndUserCriteria;
    }

    // Private Methods for Key Generation
    // ------------------------------------------------------------------------

    private List<String> generateKeys(final Integer orgId, final String projectName) {

        // Sanity Checks
        Verify.notNull(projectName, "$generateKeys :: projectName must be non NULL");
        Verify.notNull(orgId, "$generateKeys :: orgId must be non NULL");

        final String[] subStrings = projectName.split(" ");
        String key = "";
        final List<String> keys = new ArrayList<String>();

        for (String s : subStrings) {
            for (int i = 2; i < s.length(); i++) {
                keys.add(s.substring(0, i).toUpperCase());
            }
            key = key + s.charAt(0);
        }
        if (key.length() >= 2) {
            keys.add(0, key);
        }
        LOGGER.info("$generateKeys :: Generated Keys : {}", keys);
        return keys;
    }

    private boolean isKeyUnique(final String key, final Integer orgId, final int pageNo, final int pageSize) {

        // Sanity Checks
        Verify.notNull(key, "$isKeyUnique :: key must be non NULL");
        Verify.notNull(orgId, "$isKeyUnique :: orgId must be non NULL");

        final List<ProjectDTO> projectDtos = this.findByOrgIdAndKey(orgId, key, pageNo, pageSize);

        if (projectDtos.isEmpty()) {
            return true;
        }
        return false;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
