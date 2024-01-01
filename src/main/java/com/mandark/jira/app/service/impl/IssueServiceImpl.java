package com.mandark.jira.app.service.impl;

import static com.mandark.jira.app.dto.SprintDTO.DTO_DATE_FORMAT;
import static com.mandark.jira.app.enums.IssueType.BUG;
import static com.mandark.jira.app.enums.IssueType.EPIC;
import static com.mandark.jira.app.enums.IssueType.STORY;
import static com.mandark.jira.app.enums.IssueType.SUB_TASK;
import static com.mandark.jira.app.enums.IssueType.TASK;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_ASSIGNEE;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_IS_ACTIVE;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_PROJECT;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_STATUS;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_TYPE;
import static com.mandark.jira.app.persistence.orm.entity.SprintIssue.PROP_ISSUE;
import static com.mandark.jira.spi.app.SearchQuery.DATE_FORMAT_STR;
import static com.mandark.jira.web.WebConstants.DEFAULT_PAGE_NO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.bean.search.IssueSearchQuery;
import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.dto.SprintDTO;
import com.mandark.jira.app.enums.IssueStatus;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.app.persistence.orm.entity.SprintIssue;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.service.IssueService;
import com.mandark.jira.app.service.ProjectService;
import com.mandark.jira.spi.app.SearchQuery;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.lang.AuthorizationException;
import com.mandark.jira.spi.lang.ValidationException;
import com.mandark.jira.spi.util.Values;
import com.mandark.jira.spi.util.Verify;


public class IssueServiceImpl extends AbstractJpaEntityService<Issue, IssueBean, IssueDTO> implements IssueService {


    private static final Logger LOGGER = LoggerFactory.getLogger(IssueServiceImpl.class);

    private ProjectService projectService;


    public IssueServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

    @Override
    protected Class<Issue> getEntityClass() {
        return Issue.class;
    }

    @Override
    protected IssueDTO toDTO(Issue entityObj) {
        return Objects.isNull(entityObj) ? null : new IssueDTO(entityObj);
    }

    @Override
    protected Issue createFromBean(IssueBean bean) {
        return this.copyFromBean(new Issue(), bean);
    }

    @Override
    protected Issue copyFromBean(Issue exEntity, IssueBean entityBean) {

        if (Objects.isNull(exEntity) || Objects.isNull(entityBean)) {
            return exEntity;
        }

        final User beanAssignee = Objects.isNull(entityBean.getAssigneeId()) ? null
                : this.dao.read(User.class, entityBean.getAssigneeId(), true);
        exEntity.setAssignee(beanAssignee);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_STR);
        final LocalDateTime endDate = Objects.isNull(entityBean.getEndDate()) ? null
                : LocalDateTime.parse(entityBean.getEndDate(), formatter);
        exEntity.setEndDate(endDate);

        final LocalDateTime startDate = Objects.isNull(entityBean.getStartDate()) ? null
                : LocalDateTime.parse(entityBean.getStartDate(), formatter);
        exEntity.setStartDate(startDate);

        exEntity.setLabel(entityBean.getLabel());
        final Issue beanParentIssue = Objects.isNull(entityBean.getParentIssueId()) ? null
                : this.dao.read(Issue.class, entityBean.getParentIssueId(), true);
        exEntity.setParentIssue(beanParentIssue);

        exEntity.setPriority(entityBean.getIssuePriority());
        exEntity.setStatus(entityBean.getIssueStatus());
        exEntity.setSummary(entityBean.getSummary());
        exEntity.setType(entityBean.getIssueType());
        exEntity.setVersionStr(entityBean.getVersionStr());

        return exEntity;
    }

    @Override
    @Transactional
    public Integer create(final IssueBean issueBean, final int projectId, final int reporterId) {

        // Sanity Checks
        Verify.notNull(issueBean, "$create :: issueBean must be non NULL");

        final Project project = this.dao.read(Project.class, projectId, true);
        final User reportedBy = this.dao.read(User.class, reporterId, true);

        if (!projectService.isUserExist(reportedBy, project)) {

            final String msg = "The user trying to create Issue is not permitted to access the request";
            throw new AuthorizationException(msg);
        }

        final StringBuilder issueKeyBuilder = new StringBuilder();
        final Criteria projectCriteria = Criteria.equal(PROP_PROJECT, project);

        int count = super.count(projectCriteria);
        count++;

        issueKeyBuilder.append(project.getProjectKey());
        issueKeyBuilder.append("-");
        issueKeyBuilder.append(count);

        final Issue issue = this.createFromBean(issueBean);

        issue.setProject(project);
        issue.setIssueKey(issueKeyBuilder.toString());
        issue.setReportedBy(reportedBy);

        final int issueId = this.dao.save(issue);
        return issueId;
    }

    @Override
    @Transactional
    public void update(final int issueId, final IssueBean issueBean) {

        // Sanity Checks
        Verify.notNull(issueBean, "$update :: issueBean must be non NULL");

        super.update(issueId, issueBean);
    }

    @Override
    @Transactional
    public void purge(final int issueId) {

        final Issue issue = this.dao.read(this.getEntityClass(), issueId, true);
        issue.setIsActive(false);

        this.dao.update(issueId, issue);

        final String msg = String.format("$delete :: Successfully deleted the Issue with Id : %s", issueId);
        LOGGER.info(msg);
    }

    @Override
    public IssueDTO getById(final int issueId, final int projectId) {

        final Issue issue = this.dao.read(this.getEntityClass(), issueId, true);

        if (Objects.isNull(issue.getProject()) || !issue.getProject().getId().equals(projectId)) {

            final String msg = "Bad Request. Must have passed Issue belongs to the corresponding Project";
            throw new ValidationException(msg);
        }
        final IssueDTO issueDto = this.toDTO(issue);
        return issueDto;
    }

    @Override
    public List<IssueDTO> findByProjectId(final int projectId, final int pageNo, final int pageSize) {

        final Criteria projectCriteria = this.getProjectCriteria(projectId);
        final Criteria deleteCriteria = Criteria.equal(PROP_IS_ACTIVE, true);

        final Criteria andCriteria = Criteria.and(projectCriteria, deleteCriteria);

        final List<IssueDTO> issueDtos = super.find(andCriteria, pageNo, pageSize);

        return issueDtos;
    }

    @Override
    @Transactional
    public String updateAssignee(final int issueId, final Integer userId, final int projectId) {

        final Issue issueEntity = this.dao.read(this.getEntityClass(), issueId, true);
        final User userEntity = this.dao.read(User.class, userId, true);
        final Project projectEntity = this.dao.read(Project.class, projectId, true);

        if (!projectService.isUserExist(userEntity, projectEntity)) {

            final String msg = String.format(
                    "$updateAssignee :: User with Id : %s not found in the Project with Id : %s", userId, projectId);
            LOGGER.info(msg);
            throw new ValidationException(msg);
        }

        issueEntity.setAssignee(userEntity);

        this.dao.update(issueId, issueEntity);

        final String msg = String
                .format("$updateAssignee :: Successfully updated the Assignee of the Issue with Id : %s", issueId);
        LOGGER.info(msg);
        return msg;
    }

    @Override
    @Transactional
    public String removeAssignee(final int issueId, final int projectId) {

        final Issue issueEntity = this.dao.read(this.getEntityClass(), issueId, true);

        if (issueEntity.getProject().getId() != (projectId)) {

            final String msg = String.format(
                    "$updateAssignee :: Not Found Issue with Id : %s in the Project with Id : %s", issueId, projectId);
            LOGGER.info(msg);
            throw new ValidationException(msg);
        }

        issueEntity.setAssignee(null);
        this.dao.update(issueId, issueEntity);

        final String msg = String
                .format("$updateAssignee :: Successfully Removed the Assignee of the Issue with Id : %s", issueId);
        LOGGER.info(msg);
        return msg;
    }

    @Override
    @Transactional
    public String addExChildIssueToEpic(final int exIssueId, final int epicId, final int projectId) {

        final Issue exIssue = this.dao.read(this.getEntityClass(), exIssueId, true);
        final Issue epic = this.dao.read(this.getEntityClass(), epicId, true);

        if (!this.isEpic(epicId) || this.isEpic(exIssueId) || this.isSubTask(exIssueId)
                || !exIssue.getProject().getId().equals(projectId) || !epic.getProject().getId().equals(projectId)) {

            final String msg =
                    "$addExChildIssueToEpic :: Bad Request. Make sure to pass valid Issue and Epics from valid Project";
            LOGGER.info(msg);
            throw new AuthorizationException(msg);
        }

        exIssue.setParentIssue(epic);

        this.dao.update(exIssueId, exIssue);

        final String msg = String.format(
                "$addExChildIssueToEpic :: Successfully added Issue with Id : %s to the Epic with Id : %s", exIssueId,
                epicId);
        LOGGER.info(msg);
        return msg;
    }

    @Override
    @Transactional
    public String addSubTaskToNonEpic(final int subTaskId, final int nonEpicId, final int projectId) {

        final Issue subTask = this.dao.read(this.getEntityClass(), subTaskId, true);
        final Issue nonEpic = this.dao.read(this.getEntityClass(), nonEpicId, true);

        if (this.isEpic(nonEpicId) || this.isSubTask(nonEpicId) || !this.isSubTask(subTaskId)
                || !subTask.getProject().getId().equals(projectId) || !nonEpic.getProject().getId().equals(projectId)) {

            final String msg =
                    "$addExChildIssueToEpic :: Bad Request. Make sure to Pass Valid SubTaskId and NonEpicId from valid Project";
            LOGGER.info(msg);
            throw new AuthorizationException(msg);
        }

        subTask.setParentIssue(nonEpic);

        this.dao.update(subTaskId, subTask);

        final String msg = String.format(
                "$addExChildIssueToEpic :: Successfully added SubTask with Id : %s to the NonEpic Issue with Id : %s",
                subTaskId, nonEpicId);
        LOGGER.info(msg);
        return msg;
    }

    @Override
    public Map<String, Map<SprintDTO, String>> getSprintHistory(final int issueId) {

        final Issue issue = this.dao.read(this.getEntityClass(), issueId, true);
        final Criteria issueCr = Criteria.equal(PROP_ISSUE, issue);

        final int count = this.dao.count(SprintIssue.class, issueCr);
        final int pageNo = Values.get(DEFAULT_PAGE_NO, Integer::parseInt);

        final List<SprintIssue> sprintIssues = this.dao.find(SprintIssue.class, issueCr, pageNo, count);

        final Map<SprintDTO, String> passedBySprintMap = new HashedMap<>();

        final Map<SprintDTO, String> currentSprintMap = new HashedMap<>();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DTO_DATE_FORMAT);

        for (SprintIssue si : sprintIssues) {

            final Sprint sprint = si.getSprint();

            if (!si.getIsLatest()) {
                final LocalDateTime endDate = sprint.getEndDate();
                final String endDateStr = endDate.toLocalDate().format(formatter);
                final SprintDTO sprintDto = new SprintDTO(sprint);// sprint is non null field
                passedBySprintMap.put(sprintDto, endDateStr);
                continue;
            }

            final LocalDateTime endDate = sprint.getEndDate();
            if (Objects.isNull(endDate)) {
                continue; // Skipping as Sprint is not Started - INACTIVE Sprint
            }
            final String endDateStr = endDate.toLocalDate().format(formatter);
            final SprintDTO sprintDto = new SprintDTO(sprint);
            currentSprintMap.put(sprintDto, endDateStr);
        }
        final Map<String, Map<SprintDTO, String>> statusSprintsMap = new HashedMap<String, Map<SprintDTO, String>>();
        statusSprintsMap.put("PASSED BY", passedBySprintMap);
        statusSprintsMap.put("CURRENT", currentSprintMap);
        return statusSprintsMap;
    }

    @Override
    public List<IssueDTO> listValidChildsForEpic(final int projectId, final int pageNo, final int pageSize) {

        final Criteria nonEpicCriteria = this.getNonEpicCriteria(projectId);

        final List<Issue> issueList = this.dao.find(this.getEntityClass(), nonEpicCriteria, pageNo, pageSize);

        return this.toDTOs(issueList);
    }

    @Override
    public List<IssueDTO> listEpicsInProject(final int projectId, final int pageNo, final int pageSize) {

        final Criteria projectCriteria = this.getProjectCriteria(projectId);
        final Criteria epicCriteria = Criteria.equal(PROP_TYPE, EPIC);

        final Criteria andCriteria = Criteria.and(projectCriteria, epicCriteria);

        final List<Issue> issues = this.dao.find(this.getEntityClass(), andCriteria, pageNo, pageSize);
        return this.toDTOs(issues);
    }

    @Override
    public List<IssueDTO> listSubTasks(final int projectId, final int pageNo, final int pageSize) {

        final Criteria projectCriteria = this.getProjectCriteria(projectId);
        final Criteria subTaskCriteria = Criteria.equal(PROP_TYPE, SUB_TASK);

        final Criteria andCriteria = Criteria.and(projectCriteria, subTaskCriteria);

        final List<Issue> issues = this.dao.find(this.getEntityClass(), andCriteria, pageNo, pageSize);
        return this.toDTOs(issues);
    }

    @Override
    public Criteria getNonEpicCriteria(final int projectId) {

        final Criteria projectCriteria = this.getProjectCriteria(projectId);

        final List<IssueType> typeList = new ArrayList<IssueType>();
        typeList.add(STORY);
        typeList.add(BUG);
        typeList.add(TASK);

        final Criteria typeInCriteria = Criteria.in(PROP_TYPE, typeList);

        final Criteria andCriteria = Criteria.and(projectCriteria, typeInCriteria);

        return andCriteria;
    }

    private Criteria getProjectCriteria(final int projectId) {
        final Project project = this.dao.read(Project.class, projectId, true);
        final Criteria projectCriteria = Criteria.equal(PROP_PROJECT, project);
        return projectCriteria;
    }

    @Override
    public boolean isEpic(final int issueId) {
        final Issue issue = this.dao.read(Issue.class, issueId, true);
        return IssueType.EPIC.equals(issue.getType());
    }

    @Override
    public boolean isSubTask(final int issueId) {
        final Issue issue = this.dao.read(Issue.class, issueId, true);
        return IssueType.SUB_TASK.equals(issue.getType());
    }

    @Override
    public int count(final int projectId) {
        return super.count(this.getProjectCriteria(projectId));
    }

    @Override
    public int count(final int projectId, String paramName, Object paramValue) {

        final Criteria projectCriteria = this.getProjectCriteria(projectId);
        final Criteria paramCriteria = Criteria.equal(paramName, paramValue);
        final Criteria andCriteria = Criteria.and(projectCriteria, paramCriteria);

        return super.count(andCriteria);
    }

    @Override
    public int nonEpicCount(final int projectId) {
        return super.count(this.getNonEpicCriteria(projectId));
    }

    @Override
    public Criteria asCriteria(final SearchQuery<? extends Issue> searchQuery) {

        // Sanity Checks
        Verify.notNull(searchQuery, "$asCriteria :: searchQuery must be Non NULL");

        final List<Criteria> criterias = new ArrayList<>();

        final IssueSearchQuery isq = (IssueSearchQuery) searchQuery;

        final List<String> issueTypeParams = isq.getIssueType();
        final List<IssueType> issTypesList = new ArrayList<IssueType>();
        if (!issueTypeParams.isEmpty()) {
            for (String issType : issueTypeParams) {
                issTypesList.add(IssueType.valueOf(issType));
            }
            final Criteria typeInCr = Criteria.in(PROP_TYPE, issTypesList);
            criterias.add(typeInCr);
        }

        final List<String> issueStatusParams = isq.getIssueStatus();
        final List<IssueStatus> issStatusList = new ArrayList<>();
        if (!issueStatusParams.isEmpty()) {
            for (String issStatus : issueStatusParams) {
                issStatusList.add(IssueStatus.valueOf(issStatus));
            }
            final Criteria statusInCr = Criteria.in(PROP_STATUS, issStatusList);
            criterias.add(statusInCr);
        }

        final List<String> assigneeParams = isq.getAssignee();
        final List<User> assigneeList = new ArrayList<>();
        if (!assigneeParams.isEmpty()) {
            for (String assigneeId : assigneeParams) {
                final User assigneeEn = this.dao.read(User.class, Values.get(assigneeId, Integer::parseInt), true);
                assigneeList.add(assigneeEn);
            }
            final Criteria assigneeInCr = Criteria.in(PROP_ASSIGNEE, assigneeList);
            criterias.add(assigneeInCr);
        }

        final Criteria cr = Criteria.and(criterias);
        return cr;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

}
