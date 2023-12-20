package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.service.EntityService;


public interface IssueService extends EntityService<Integer, Issue, IssueDTO> {

    int create(final IssueBean issueBean, final int projectId, final int reporterId);

    void update(final int issueId, final IssueBean issueBean);

    IssueDTO getById(final int issueId);

    List<IssueDTO> readAllByProjectId(final int projectId, final int pageNo, final int pageSize);

    void updateAssignee(final int issueId, final int userId);

    void addExChildIssueToEpic(final int exIssueId, final int epicId);

    void delete(final int issueId);


    List<IssueDTO> listValidChildsForEpic(final int projectId, final int pageNo, final int pageSize);

    List<IssueDTO> listEpicsInProject(final int projectId, final int pageNo, final int pageSize);

    List<IssueDTO> listSubTasks(final int projectId, final int pageNo, final int pageSize);


    int count(final int projectId);

    boolean isEpic(final int epicId);

}
