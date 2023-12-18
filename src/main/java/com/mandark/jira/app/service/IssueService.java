package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.IssueBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.service.EntityService;


public interface IssueService extends EntityService<Integer, Issue, IssueDTO> {

    int create(final IssueBean issueBean, final Integer projectId, final Integer reporterId);

    void update(final Integer issueId, final IssueBean issueBean);

    IssueDTO getById(final Integer issueId);

    List<IssueDTO> readAllByProjectId(Integer projectId, int pageNo, int pageSize);

    void updateAssignee(Integer issueId, Integer userId);


    void addExChildIssueToEpic(Integer exIssueId, int epicId);

    // void createChildI



    void addExIssueToEpic();

    void addChildIssueOfNonEpic();

    void addPatrentEpic();

    void addExistingSubTask();

    // void delete(Integer issueId);


}
