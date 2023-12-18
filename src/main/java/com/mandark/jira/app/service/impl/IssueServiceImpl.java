package com.mandark.jira.app.service.impl;

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
        exEntity.setAssignee(entityBean.getAssignee());
        exEntity.setEndDate(entityBean.getEndDate());
        exEntity.setLabel(entityBean.getLabel());
        exEntity.setParentIssueId(entityBean.getParentIssueId());

        List<Sprint> sprints = new ArrayList<Sprint>();
        sprints.add(entityBean.getSprint());
        exEntity.setSprint(sprints);

        exEntity.setPriority(entityBean.getPriority());
        exEntity.setStartDate(entityBean.getStartDate());
        exEntity.setStatus(entityBean.getStatus());
        exEntity.setSummary(entityBean.getSummary());
        exEntity.setType(entityBean.getType());
        exEntity.setVersionStr(entityBean.getVersionStr());

        return exEntity;
    }

    @Override
    @Transactional
    public int create(final IssueBean issueBean, final Integer projectId, final Integer reporterId) {

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
    public void update(Integer issueId, IssueBean issueBean) {

        // Sanity Checks
        Verify.notNull(issueBean, "$update :: issueBean must be non NULL");
        Verify.notNull(issueId, "$update :: issueId must be non NULL");

        super.update(issueId, issueBean);
    }

    @Override
    public IssueDTO getById(final Integer issueId) {

        // Sanity Checks
        Verify.notNull(issueId, "$getById :: issueId must be non NULL");

        final Issue issue = this.dao.read(this.getEntityClass(), issueId, true);
        final IssueDTO issueDto = this.toDTO(issue);

        return issueDto;
    }

    @Override
    public List<IssueDTO> readAllByProjectId(Integer projectId, int pageNo, int pageSize) {

        final Project project = this.dao.read(Project.class, projectId, true);
        final Criteria criteria = Criteria.equal(Issue.PROP_PROJECT, project);

        List<IssueDTO> issueDtos = super.find(criteria, pageNo, pageSize);

        return issueDtos;
    }

    @Override
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
    public void addExChildIssueToEpic(Integer exIssueId, int epicId) {

        final Issue exIssue = this.dao.read(Issue.class, exIssueId, true);

        exIssue.setParentIssueId(epicId);

        this.dao.update(exIssueId, exIssue);

        LOGGER.info("$addExChildIssueToEpic :: Successfully Added Issue with Id : {} to the Epic with Id : {}",
                exIssueId, epicId);


    }
    
    private void listExChildIssue(Integer projectId) {
        
        
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
