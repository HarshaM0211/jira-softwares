package com.mandark.jira.app.service;

import java.util.List;
import java.util.Map;

import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.dto.SprintDTO;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.EntityService;


public interface IssueService extends EntityService<Integer, Issue, IssueDTO> {

    Integer create(final IssueBean issueBean, final int projectId, final int reporterId);

    void update(final int issueId, final IssueBean issueBean);

    void purge(final int issueId);

    IssueDTO getById(final int issueId, final int projectId);

    List<IssueDTO> findByProjectId(final int projectId, final int pageNo, final int pageSize);

    String updateAssignee(final int issueId, final Integer userId, final int projectId);

    String removeAssignee(final int issueId, final int projectId);

    String addExChildIssueToEpic(final int exIssueId, final int epicId, final int projectId);

    String addSubTaskToNonEpic(final int subTaskId, final int nonEpicId, final int projectId);


    Map<String, List<SprintDTO>> getSprintHistory(final int issueId);


    List<IssueDTO> listValidChildsForEpic(final int projectId, final int pageNo, final int pageSize);

    List<IssueDTO> listEpicsInProject(final int projectId, final int pageNo, final int pageSize);

    List<IssueDTO> listSubTasks(final int projectId, final int pageNo, final int pageSize);


    int count(final int projectId);

    int count(final int projectId, final String paramName, final Object paramValue);

    int nonEpicCount(final int projectId);

    boolean isEpic(final int epicId);

    boolean isSubTask(final int issueId);

    Criteria getNonEpicCriteria(final int projectId);
}
