package com.mandark.jira.app.service.impl;

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
import static com.mandark.jira.spi.app.SearchQuery.DATE_FORMAT_STR;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.enums.IssuePriority;
import com.mandark.jira.app.enums.IssueStatus;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.search.bean.IssueSearchQuery;
import com.mandark.jira.app.service.IssueService;
import com.mandark.jira.spi.app.SearchQuery;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class IssueServiceImpl extends AbstractJpaEntityService<Issue, IssueBean, IssueDTO> implements IssueService {


    private static final Logger LOGGER = LoggerFactory.getLogger(IssueServiceImpl.class);


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

        User beanAssignee = Objects.isNull(entityBean.getAssigneeId()) ? null
                : this.dao.read(User.class, entityBean.getAssigneeId(), true);
        exEntity.setAssignee(beanAssignee);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_STR);
        LocalDateTime endDate = Objects.isNull(entityBean.getEndDate()) ? null
                : LocalDateTime.parse(entityBean.getEndDate(), formatter);
        exEntity.setEndDate(endDate);

        LocalDateTime startDate = Objects.isNull(entityBean.getStartDate()) ? null
                : LocalDateTime.parse(entityBean.getStartDate(), formatter);
        exEntity.setStartDate(startDate);

        exEntity.setLabel(entityBean.getLabel());
        exEntity.setParentIssueId(entityBean.getParentIssueId());

        List<Sprint> sprints = new ArrayList<Sprint>();
        Sprint beanSprint = Objects.isNull(entityBean.getSprintId()) ? null
                : this.dao.read(Sprint.class, entityBean.getSprintId(), true);
        sprints.add(beanSprint);
        exEntity.setSprint(sprints);

        exEntity.setPriority(IssuePriority.valueOf(entityBean.getPriorityStr()));
        exEntity.setStatus(IssueStatus.valueOf(entityBean.getStatusStr()));
        exEntity.setSummary(entityBean.getSummary());
        exEntity.setType(IssueType.valueOf(entityBean.getTypeStr()));
        exEntity.setVersionStr(entityBean.getVersionStr());

        return exEntity;
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
                final User assigneeEn = this.dao.read(User.class, Integer.parseInt(assigneeId), true);
                assigneeList.add(assigneeEn);
            }
            final Criteria assigneeInCr = Criteria.in(PROP_ASSIGNEE, assigneeList);
            criterias.add(assigneeInCr);
        }

        final Criteria cr = Criteria.and(criterias);
        return cr;
    }


    @Override
    @Transactional
    public int create(final IssueBean issueBean, final int projectId, final int reporterId) {

        // Sanity Checks
        Verify.notNull(issueBean, "$create :: issueBean must be non NULL");

        final Project project = this.dao.read(Project.class, projectId, true);
        final User reportedBy = this.dao.read(User.class, reporterId, true);

        final Criteria projectCriteria = Criteria.equal(PROP_PROJECT, project);
        int count = super.count(projectCriteria);
        count++;
        final String issueKey = project.getProjectKey() + "-" + count;

        final Issue issue = this.createFromBean(issueBean);

        issue.setProject(project);
        issue.setIssueKey(issueKey);
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
    public IssueDTO getById(final int issueId) {

        final Issue issue = this.dao.read(this.getEntityClass(), issueId, true);
        final IssueDTO issueDto = this.toDTO(issue);

        return issueDto;
    }

    @Override
    public List<IssueDTO> readAllByProjectId(final int projectId, final int pageNo, final int pageSize) {

        final Criteria projectCriteria = this.getProjectCriteria(projectId);
        final Criteria deleteCriteria = Criteria.equal(PROP_IS_ACTIVE, true);

        final Criteria andCriteria = Criteria.and(projectCriteria, deleteCriteria);

        final List<IssueDTO> issueDtos = super.find(andCriteria, pageNo, pageSize);

        return issueDtos;
    }

    @Override
    @Transactional
    public void updateAssignee(final int issueId, final int userId) {

        final Issue issueEntity = this.dao.read(this.getEntityClass(), issueId, true);
        final User newAssignee = this.dao.read(User.class, userId, true);

        issueEntity.setAssignee(newAssignee);

        this.dao.update(issueId, issueEntity);

        final String msg = String
                .format("$updateAssignee :: Successfully updated the Assignee of the Issue with Id : %s", issueId);
        LOGGER.info(msg);

    }

    @Override
    @Transactional
    public void addExChildIssueToEpic(final int exIssueId, final int epicId) {

        final Issue exIssue = this.dao.read(Issue.class, exIssueId, true);

        exIssue.setParentIssueId(epicId);

        this.dao.update(exIssueId, exIssue);

        LOGGER.info("$addExChildIssueToEpic :: Successfully added Issue with Id : {} to the Epic with Id : {}",
                exIssueId, epicId);
    }

    @Override
    public boolean isEpic(final int epicId) {
        final Issue issue = this.dao.read(Issue.class, epicId, true);
        return IssueType.EPIC.equals(issue.getType());
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
    @Transactional
    public void purge(final int issueId) {

        final Issue issue = this.dao.read(this.getEntityClass(), issueId, true);
        issue.setIsActive(false);

        this.dao.update(issueId, issue);

        final String msg = String.format("$delete :: Successfully deleted the Issue with Id : %s", issueId);
        LOGGER.info(msg);
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
    public List<IssueDTO> listValidChildsForEpic(final int projectId, final int pageNo, final int pageSize) {

        final Criteria nonEpicCriteria = this.getNonEpicCriteria(projectId);

        final List<Issue> issueList = this.dao.find(this.getEntityClass(), nonEpicCriteria, pageNo, pageSize);

        return this.toDTOs(issueList);
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

}
