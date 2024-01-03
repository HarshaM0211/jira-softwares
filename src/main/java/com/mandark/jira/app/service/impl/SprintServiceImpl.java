package com.mandark.jira.app.service.impl;

import static com.mandark.jira.app.enums.IssueStatus.DONE;
import static com.mandark.jira.app.enums.IssueType.EPIC;
import static com.mandark.jira.app.enums.IssueType.SUB_TASK;
import static com.mandark.jira.app.enums.SprintStatus.ACTIVE;
import static com.mandark.jira.app.enums.SprintStatus.COMPLETED;
import static com.mandark.jira.app.enums.SprintStatus.INACTIVE;
import static com.mandark.jira.app.persistence.orm.entity.Sprint.PROP_PROJECT;
import static com.mandark.jira.app.persistence.orm.entity.Sprint.PROP_STATUS;
import static com.mandark.jira.app.persistence.orm.entity.SprintIssue.PROP_ISSUE;
import static com.mandark.jira.app.persistence.orm.entity.SprintIssue.PROP_IS_LATEST;
import static com.mandark.jira.app.persistence.orm.entity.SprintIssue.PROP_SPRINT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.SprintBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.dto.SprintDTO;
import com.mandark.jira.app.enums.IssueType;
import com.mandark.jira.app.enums.SprintStatus;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.Project;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.app.persistence.orm.entity.SprintIssue;
import com.mandark.jira.app.service.SprintService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.lang.ValidationException;
import com.mandark.jira.spi.util.Values;
import com.mandark.jira.spi.util.Verify;
import com.mandark.jira.web.WebConstants;


public class SprintServiceImpl extends AbstractJpaEntityService<Sprint, SprintBean, SprintDTO>
        implements SprintService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintServiceImpl.class);

    public SprintServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

    @Override
    protected Class<Sprint> getEntityClass() {
        return Sprint.class;
    }

    @Override
    protected SprintDTO toDTO(Sprint entityObj) {
        return Objects.isNull(entityObj) ? null : new SprintDTO(entityObj);
    }

    @Override
    protected Sprint createFromBean(SprintBean bean) {
        return this.copyFromBean(new Sprint(), bean);
    }

    @Override
    protected Sprint copyFromBean(Sprint exEntity, SprintBean entityBean) {

        if (Objects.isNull(exEntity) || Objects.isNull(entityBean)) {
            return exEntity;
        }

        if (Objects.nonNull(entityBean.getSprintKey())) {
            exEntity.setSprintKey(entityBean.getSprintKey());
        }
        if (Objects.nonNull(entityBean.getStartDate())) {
            exEntity.setStartDate(entityBean.getStartDate());
        }
        if (Objects.nonNull(entityBean.getEndDate())) {
            exEntity.setEndDate(entityBean.getEndDate());
        }
        return exEntity;
    }

    @Override
    @Transactional
    public int create(final int projectId, final SprintBean sprintBean) {

        // Sanity Checks
        Verify.notNull(sprintBean, "$create :: sprintBean must be non NULL");

        final Sprint sprintEntity = this.createFromBean(sprintBean);
        final Project project = super.readEntity(Project.class, projectId, true);

        final Criteria projectCriteria = Criteria.equal(PROP_PROJECT, project);
        int count = super.count(projectCriteria);
        count++;

        sprintEntity.setProject(project);
        sprintEntity.setStatus(INACTIVE);


        final String beanKey = sprintEntity.getSprintKey();

        if (Objects.nonNull(beanKey)) {

            sprintEntity.setSprintKey(beanKey);
            final int sprintId = this.dao.save(sprintEntity);
            return sprintId;
        }
        final StringBuilder sprintKeyBuilder = new StringBuilder();
        sprintKeyBuilder.append(project.getProjectKey());
        sprintKeyBuilder.append(" Sprint ");
        sprintKeyBuilder.append(String.valueOf(count));

        sprintEntity.setSprintKey(sprintKeyBuilder.toString());

        final int sprintId = this.dao.save(sprintEntity);

        final String msg = String.format("Successfully Created a Sprint with Id : %s", sprintId);
        LOGGER.info(msg);

        return sprintId;
    }

    @Override
    @Transactional
    public void update(final int sprintId, final SprintBean sprintBean) {

        // Sanity Checks
        Verify.notNull(sprintBean, "$update :: sprintBean must be non NULL");

        super.update(sprintId, sprintBean);

        final String msg = String.format("Successfully Updated a Sprint with Id : %s", sprintId);
        LOGGER.info(msg);
    }

    @Override
    public List<SprintDTO> getByProjectId(final int projectId) {

        final Project project = super.readEntity(Project.class, projectId, true);

        final Criteria projectCriteria = Criteria.equal(PROP_PROJECT, project);
        final List<SprintStatus> statusList = new ArrayList<>();
        statusList.add(INACTIVE);
        statusList.add(ACTIVE);
        final Criteria inCriteria = Criteria.in(PROP_STATUS, statusList);
        final Criteria projectAndStatusCr = Criteria.and(projectCriteria, inCriteria);

        final int count = this.dao.count(this.getEntityClass(), projectAndStatusCr);

        final List<SprintDTO> sprintDtos = super.find(projectAndStatusCr, 1, count);

        return sprintDtos;
    }

    @Override
    @Transactional
    public void start(final int sprintId) {

        final Sprint sprint = super.readEntity(this.getEntityClass(), sprintId, true);

        if (Objects.isNull(sprint.getEndDate()) || Objects.isNull(sprint.getStartDate())) {

            throw new ValidationException("Please add dates for Sprint before Starting");
        }
        sprint.setStatus(ACTIVE);
        this.dao.update(sprintId, sprint);
    }

    @Override
    @Transactional
    public String complete(final int sprintId, final int nextSprintId) {

        final List<Integer> unDoneIssueIds = this.getUndoneIssueIds(sprintId);
        // Adding UnDone Issues to next Sprint
        if (!unDoneIssueIds.isEmpty() && Objects.nonNull(nextSprintId)) {
            this.addIssues(unDoneIssueIds, nextSprintId);
        }
        // Updating current Sprint as Completed
        final Sprint sprint = super.readEntity(this.getEntityClass(), sprintId, true);
        sprint.setStatus(COMPLETED);
        this.dao.update(sprintId, sprint);
        final String msg = String.format("Successfully Completed the Sprint with Id : %s", sprintId);
        LOGGER.info(msg);
        return msg;
    }

    @Override
    public boolean isIssuesDone(final int sprintId) {

        final List<Integer> unDoneIssueIds = this.getUndoneIssueIds(sprintId);
        return unDoneIssueIds.isEmpty();
    }

    private List<Integer> getUndoneIssueIds(final int sprintId) {

        final List<IssueDTO> issueDtos = this.getIssues(sprintId);

        final List<Integer> unDoneIssueIds = new ArrayList<>();

        for (IssueDTO issueDto : issueDtos) {

            final int issueId = Values.get(issueDto.getId(), Integer::parseInt);

            if (!issueDto.getStatusStr().equals(DONE.toString())) {
                unDoneIssueIds.add(issueId);
                continue;
            }
            // Issue Done

            // Checking childs
            final Issue issue = super.readEntity(Issue.class, issueId, true);

            final Criteria parentCriteria = Criteria.equal(Issue.PROP_PARENT_ISSUE, issue);

            final int count = this.dao.count(Issue.class, parentCriteria);
            final int pageNo = Values.get(WebConstants.DEFAULT_PAGE_NO, Integer::parseInt);

            final List<Issue> childIssues = this.dao.find(Issue.class, parentCriteria, pageNo, count);

            if (childIssues.isEmpty()) {
                LOGGER.info("Issue with Id : {} have no Child Issues", issueId);
                continue;
            }
            for (Issue childIssue : childIssues) {

                if (DONE.equals(childIssue.getStatus())) {
                    continue;
                }
                final String msg = String.format("Can't Complete the Sprint. Found InCompleted Sub Task of Id : %s",
                        childIssue.getId());
                throw new ValidationException(msg);
            }
            // Child Issues Done
        }
        return unDoneIssueIds;
    }

    @Override
    @Transactional
    public String removeIssue(final int issueId) {
        final Issue issue = super.readEntity(Issue.class, issueId, true);
        final String msg = this.removeIssue(issue);
        return msg;
    }

    private String removeIssue(final Issue issue) {

        // Sanity Checks
        Verify.notNull(issue, "$removeIssue :: issue must be non NULL");

        final Criteria issueCriteria = Criteria.equal(PROP_ISSUE, issue);
        final Criteria latestCriteria = Criteria.equal(PROP_IS_LATEST, true);
        final Criteria andCriteria = Criteria.and(issueCriteria, latestCriteria);

        final SprintIssue sprintIssue = this.dao.findOne(SprintIssue.class, andCriteria);

        if (Objects.isNull(sprintIssue)) {
            final String msg = "Currently, the Issue doesn't belongs to any of the Sprints";
            LOGGER.info(msg);
            return msg;
        }
        sprintIssue.setIsLatest(false);
        final int id = sprintIssue.getId();

        this.dao.update(id, sprintIssue);

        final String msg = String.format("Successfully removed Issue with Id : %s from it's Sprint", issue.getId());
        LOGGER.info(msg);
        return msg;
    }

    @Override
    @Transactional
    public void addIssues(final List<Integer> issueIds, final int sprintId) {

        // Sanity Checks
        Verify.notEmpty(issueIds, "$addIssues :: issueIds must have atleast One element");

        final Sprint sprint = this.dao.read(this.getEntityClass(), sprintId, true);

        final List<Issue> issueEntities = this.dao.read(Issue.class, issueIds);

        for (Issue issue : issueEntities) {

            final boolean isValid = this.isValid(issue);
            if (!isValid) {
                LOGGER.info("Invalid IssueType! Skipped adding of Issue with Id : {} into the Sprint with Id : {}",
                        issue.getId(), sprintId);
                continue;
            }

            this.removeIssue(issue);

            final SprintIssue sprintIssue = new SprintIssue();
            sprintIssue.setSprint(sprint);
            sprintIssue.setIssue(issue);
            sprintIssue.setIsLatest(true);
            final int id = this.dao.save(sprintIssue);

            final String msg = String.format("Issue with Id : %s, is added into Sprint with Id : %s", issue.getId(),
                    sprint.getId());
            LOGGER.info("Created new SprintIssue record with Id : {}", id);
            LOGGER.info(msg);
        }
    }

    private boolean isValid(Issue issue) {
        final IssueType issueType = issue.getType();
        if (issueType.equals(EPIC) || issueType.equals(SUB_TASK)) {
            return false;

        }
        return true;
    }

    @Override
    public List<IssueDTO> getIssues(final int sprintId) {

        final Sprint sprint = super.readEntity(this.getEntityClass(), sprintId, true);

        final Criteria sprintCriteria = Criteria.equal(PROP_SPRINT, sprint);
        final Criteria latestCriteria = Criteria.equal(PROP_IS_LATEST, true);
        final Criteria andCriteria = Criteria.and(sprintCriteria, latestCriteria);

        final int count = this.dao.count(SprintIssue.class, andCriteria);
        final int pageNo = Values.get(WebConstants.DEFAULT_PAGE_NO, Integer::parseInt);

        final List<SprintIssue> sprintIssues = this.dao.find(SprintIssue.class, andCriteria, pageNo, count);

        final List<IssueDTO> issueDtos = new ArrayList<>();
        for (SprintIssue sprintIssue : sprintIssues) {
            issueDtos.add(Values.get(sprintIssue.getIssue(), IssueDTO::new));
        }
        return issueDtos;
    }
}
