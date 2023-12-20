package com.mandark.jira.app.search.bean;

import java.util.List;
import java.util.Map;

import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.SearchQuery;


public class IssueSearchQuery extends SearchQuery<Issue> {

    public static final String KEY_ISSUE_TYPE = "type";

    public static final String KEY_ISSUE_STATUS = "status";

    public static final String KEY_ASSIGNEE = "assigneeId";

    final private List<String> issueType;

    final private List<String> issueStatus;

    final private List<String> assigneeIds;


    public IssueSearchQuery(Map<String, List<String>> criteria) {

        super(criteria);

        this.issueType = super.getList(KEY_ISSUE_TYPE);
        this.issueStatus = super.getList(KEY_ISSUE_STATUS);
        this.assigneeIds = super.getList(KEY_ASSIGNEE);

    }

    public List<String> getIssueType() {
        return issueType;
    }

    public List<String> getIssueStatus() {
        return issueStatus;
    }

    public List<String> getAssignee() {
        return assigneeIds;
    }

}
