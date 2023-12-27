package com.mandark.jira.app.service.impl;

import static com.mandark.jira.app.persistence.orm.entity.Project.PROP_ORGANISATION;
import static com.mandark.jira.app.persistence.orm.entity.Project.PROP_PROJECT_KEY;
import static com.mandark.jira.app.persistence.orm.entity.ProjectUser.PROP_PROJECT;
import static com.mandark.jira.app.persistence.orm.entity.ProjectUser.PROP_USER;
import static com.mandark.jira.web.WebConstants.DEFAULT_PAGE_NO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

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

        final Project project = this.createFromBean(entityBean);
        project.setOrganisation(organisation);

        final int projectId = dao.save(project);
        final String msg = String.format("$ServiceImpl :: Successfully created the Project with ID : %s", projectId);
        LOGGER.info(msg);

        return projectId;
    }

    @Override
    public Map<String, Object> getKeyAuto(final Integer orgId, final String projectName) {

        // Sanity Checks
        Verify.notNull(orgId, "$getKeyAuto :: orgId must be non NULL");
        Verify.notNull(projectName, "$getKeyAuto :: projectName must be non NULL");

        Map<String, Object> keyMap = new HashMap<String, Object>();

        final List<String> keyList = this.generateKeys(orgId, projectName);
        for (String key : keyList) {
            final int projectsCount = this.count(orgId);
            final int pageNo = Integer.parseInt(DEFAULT_PAGE_NO);
            if (this.isKeyUnique(key, orgId, pageNo, projectsCount)) {
                LOGGER.info("Key :{} is unique for given Project Name", key);
                keyMap.put(PROP_PROJECT_KEY, key);
                return keyMap;
            }
        }
        keyMap.put(PROP_PROJECT_KEY, generateRandomString(4));
        return keyMap;
    }

    private String generateRandomString(final int length) {
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();

        for (int i = 0; i < length; i++) {
            final char randomChar = (char) (random.nextInt(26) + 'A');
            sb.append(randomChar);
        }
        return sb.toString();
    }

    @Override
    public List<ProjectDTO> findByOrgIdAndKey(final Integer orgId, final String projectKey, final int pageNo,
            final int pageSize) {

        // Sanity Checks
        Verify.notNull(orgId, "$findByOrgIdAndKey :: orgId must be non NULL");
        Verify.notNull(projectKey, "$findByOrgIdAndKey :: projectKey must be non NULL");

        final Organisation organisation = dao.read(Organisation.class, orgId, true);

        final Criteria orgCriteria = Criteria.equal(PROP_ORGANISATION, organisation);
        final Criteria keyCriteria = Criteria.equal(PROP_PROJECT_KEY, projectKey);

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

        final User userEntity = dao.read(User.class, userId, true);
        final Project projectEntity = dao.read(Project.class, projectId, true);

        final ProjectUser projectUser =
                dao.findOne(ProjectUser.class, this.getProjectUsersCriteria(projectEntity, userEntity));

        if (Objects.nonNull(projectUser)) {
            LOGGER.info("User with Id : %s, already Exists in the specified Project with Id : %s", userId, projectId);
            return;
        }

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
        final Criteria projOrgCriteria = Criteria.equal(PROP_ORGANISATION, organisation);

        final List<Project> projects = dao.find(this.getEntityClass(), projOrgCriteria, pageNo, pageSize);

        final List<ProjectDTO> projectDTOs = super.toDTOs(projects);
        return projectDTOs;
    }

    @Override
    public List<ProjectDTO> findByUserId(final Integer userId, final int pageNo, final int pageSize) {

        // Sanity Checks
        Verify.notNull(userId, "$getProjectsByUserId :: userId must be non NULL");

        final User user = dao.read(User.class, userId, true);
        final Criteria criteria = Criteria.equal(PROP_USER, user);

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

        final User userEntity = dao.read(User.class, userId, true);
        final Project projectEntity = dao.read(Project.class, projectId, true);

        final ProjectUser projectUser =
                dao.findOne(ProjectUser.class, this.getProjectUsersCriteria(projectEntity, userEntity));

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
    public boolean isUserExist(final User userEntity, final Project projectEntity) {

        // Sanity Checks
        Verify.notNull(userEntity, "$isUserInOrg :: userEntity must be non NULL");
        Verify.notNull(projectEntity, "$isUserInOrg :: projectEntity must be non NULL");

        final Criteria projectCriteria = Criteria.equal(PROP_PROJECT, projectEntity);
        final Criteria userCriteria = Criteria.equal(PROP_USER, userEntity);
        final Criteria andCr = Criteria.and(projectCriteria, userCriteria);

        final ProjectUser pu = this.dao.findOne(ProjectUser.class, andCr);

        return Objects.nonNull(pu);

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

    public Criteria getProjectUsersCriteria(final Project projectEntity, final User userEntity) {

        // Sanity Checks
        Verify.notNull(projectEntity, "$getProjectUsersCriteria :: projectEntity must be non NULL");
        Verify.notNull(userEntity, "$getProjectUsersCriteria :: userEntity must be non NULL");

        final Criteria projectcriteria = Criteria.equal(PROP_PROJECT, projectEntity);
        final Criteria userCriteria = Criteria.equal(PROP_USER, userEntity);

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
        Verify.hasLength(projectName, "$generateKeys :: projectName must have atleast a Single Character");
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
            keys.add(0, key.toUpperCase());
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
