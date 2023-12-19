package com.mandark.jira.app.service.impl;

import static com.mandark.jira.app.enums.IssueType.BUG;
import static com.mandark.jira.app.enums.IssueType.STORY;
import static com.mandark.jira.app.enums.IssueType.TASK;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_PROJECT;
import static com.mandark.jira.app.persistence.orm.entity.Issue.PROP_TYPE;

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
import com.mandark.jira.app.service.IssueService;
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
        return new IssueDTO(entityObj);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
    @Transactional
    public int create(final IssueBean issueBean, final int projectId, final int reporterId) {

        // Sanity Checks
        Verify.notNull(issueBean, "$create :: issueBean must be non NULL");

        final Project project = this.dao.read(Project.class, projectId, true);
        final User reportedBy = this.dao.read(User.class, reporterId, true);

        final Criteria projectCriteria = Criteria.equal(Issue.PROP_PROJECT, project);
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
    public void update(final Integer issueId, final IssueBean issueBean) {

        // Sanity Checks
        Verify.notNull(issueBean, "$update :: issueBean must be non NULL");
        Verify.notNull(issueId, "$update :: issueId must be non NULL");

        super.update(issueId, issueBean);
    }

    @Override
    public IssueDTO getById(final int issueId) {

        // Sanity Checks
        Verify.notNull(issueId, "$getById :: issueId must be non NULL");

        final Issue issue = this.dao.read(this.getEntityClass(), issueId, true);
        final IssueDTO issueDto = this.toDTO(issue);

        return issueDto;
    }

    @Override
    public List<IssueDTO> readAllByProjectId(final Integer projectId, final int pageNo, final int pageSize) {

        final Project project = this.dao.read(Project.class, projectId, true);
        final Criteria criteria = Criteria.equal(Issue.PROP_PROJECT, project);

        final List<IssueDTO> issueDtos = super.find(criteria, pageNo, pageSize);

        return issueDtos;
    }

    @Override
    @Transactional
    public void updateAssignee(final Integer issueId, final Integer userId) {

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
    public void addExChildIssueToEpic(final Integer exIssueId, final int epicId) {

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
    public List<IssueDTO> listValidChildsForEpic(final Integer projectId, final int pageNo, final int pageSize) {

        final Project project = this.dao.read(Project.class, projectId, true);

        final Criteria projectCriteria = Criteria.equal(PROP_PROJECT, project);

        final List<IssueType> typeList = new ArrayList<IssueType>();
        typeList.add(IssueType.STORY);
        typeList.add(IssueType.BUG);
        typeList.add(IssueType.TASK);
        final Criteria typeInCriteria = Criteria.in(PROP_TYPE, typeList);

        final List<IssueStatus> statusList = new ArrayList<IssueStatus>();
        statusList.add(IssueStatus.TODO);
        statusList.add(IssueStatus.IN_PROGRESS);
        statusList.add(IssueStatus.IN_REVIEW);
        final Criteria statusInCriteria = Criteria.in(Issue.PROP_STATUS, statusList);

        final List<Criteria> criterias = new ArrayList<Criteria>();
        criterias.add(projectCriteria);
        criterias.add(typeInCriteria);
        criterias.add(statusInCriteria);


        final Criteria andCriteria = Criteria.and(criterias);

        final List<Issue> issueList = this.dao.find(this.getEntityClass(), andCriteria, pageNo, pageSize);
        final List<IssueDTO> issueDtos = this.toDTOs(issueList);

        return issueDtos;

    }

    @Override
    public int count(final int projectId) {
        final Project project = this.dao.read(Project.class, projectId, true);
        final Criteria projectCriteria = Criteria.equal(PROP_PROJECT, project);
        return super.count(projectCriteria);
    }

    @Override
    public void addPatrentEpic() {
        // TODO Auto-generated method stub
    }

    @Override
    public void addExIssueToEpic() {
        // TODO Auto-generated method stub

    }

    @Override
    public void addChildIssueOfNonEpic() {
        // TODO Auto-generated method stub

    }

    @Override
    public void addExistingSubTask() {
        // TODO Auto-generated method stub

    }

    // @Override
    // @Transactional
    // public void delete(Integer issueId) {
    //
    // // Sanity Checks
    // Verify.notNull(issueId, "$delete :: issueId miust be non NULL");
    //
    // super.purge(issueId);
    //
    // final String msg = String.format("$delete :: Successfully deleted the Issue with Id : %s", issueId);
    // LOGGER.info(msg);
    // }


}
