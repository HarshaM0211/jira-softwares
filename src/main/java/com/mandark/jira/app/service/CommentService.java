package com.mandark.jira.app.service;

import java.util.List;

import com.mandark.jira.app.beans.CommentBean;
import com.mandark.jira.app.dto.CommentDTO;
import com.mandark.jira.app.persistence.orm.entity.Comment;
import com.mandark.jira.spi.app.service.EntityService;


public interface CommentService extends EntityService<Integer, Comment, CommentDTO> {

    int add(final int issueId, final int commenterId, final CommentBean commentBean);

    String edit(final int commentId, final CommentBean commentBean);

    String delete(final int commentId);

    List<CommentDTO> getByIssueId(final int issueId);

}
