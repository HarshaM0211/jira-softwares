package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.service.EntityService;


public interface IssueService extends EntityService<Integer, Issue, IssueDTO> {

    int create(final IssueBean issueBean, final int projectId, final int reporterId);

    void update(final Integer issueId, final IssueBean issueBean);

    IssueDTO getById(final int issueId);

    List<IssueDTO> readAllByProjectId(final Integer projectId, final int pageNo, final int pageSize);

    void updateAssignee(final Integer issueId, final Integer userId);


    void addExChildIssueToEpic(final Integer exIssueId, final int epicId);

    List<IssueDTO> listValidChildsForEpic(final Integer projectId, final int pageNo, final int pageSize);

    // void createChildI



    void addExIssueToEpic();

    void addChildIssueOfNonEpic();

    void addPatrentEpic();

    void addExistingSubTask();

    // void delete(Integer issueId);

    int count(final int projectId);

    boolean isEpic(final int epicId);


}
