package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.SprintBean;
import com.mandark.jira.app.dto.IssueDTO;
import com.mandark.jira.app.dto.SprintDTO;
import com.mandark.jira.app.persistence.orm.entity.Sprint;
import com.mandark.jira.spi.app.service.EntityService;


public interface SprintService extends EntityService<Integer, Sprint, SprintDTO> {

    int create(final int projectId, final SprintBean sprintBean);

    void update(final int sprintId, final SprintBean sprintBean);

    void delete(final int sprintId);

    List<SprintDTO> getByProjectId(final int projectId);

    List<IssueDTO> getIssues(final int sprintId);

    void start(final int sprintId);

    String complete(final int sprintId, final int nextSprintId);

    void addIssues(final List<Integer> issueIds, final int sprintId);

    String removeIssue(final int issueId);

    boolean isIssuesDone(final int sprintId);

}
