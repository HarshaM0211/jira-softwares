package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.EntityService;


public interface IssueService extends EntityService<Integer, Issue, IssueDTO> {

    int create(final IssueBean issueBean, final int projectId, final int reporterId);

    void update(final int issueId, final IssueBean issueBean);

    void purge(final int issueId);

    IssueDTO getById(final int issueId);

    List<IssueDTO> readAllByProjectId(final int projectId, final int pageNo, final int pageSize);

    void updateAssignee(final int issueId, final int userId);

    void addExChildIssueToEpic(final int exIssueId, final int epicId);



    List<IssueDTO> listValidChildsForEpic(final int projectId, final int pageNo, final int pageSize);

    List<IssueDTO> listEpicsInProject(final int projectId, final int pageNo, final int pageSize);

    List<IssueDTO> listSubTasks(final int projectId, final int pageNo, final int pageSize);


    int count(final int projectId);

    int count(final int projectId, String paramName, Object paramValue);

    int nonEpicCount(final int projectId);

    boolean isEpic(final int epicId);

    Criteria getNonEpicCriteria(final int projectId);
}
