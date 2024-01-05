package com.mandark.jira.app.service.impl;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.beans.CommentBean;
import com.mandark.jira.app.dto.CommentDTO;
import com.mandark.jira.app.persistence.orm.entity.Comment;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.app.service.CommentService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class CommentServiceImpl extends AbstractJpaEntityService<Comment, CommentBean, CommentDTO>
        implements CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    public CommentServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

    @Override
    protected Class<Comment> getEntityClass() {
        return Comment.class;
    }

    @Override
    protected CommentDTO toDTO(Comment entityObj) {
        return Objects.isNull(entityObj) ? null : new CommentDTO(entityObj);
    }

    @Override
    protected Comment createFromBean(CommentBean bean) {
        return this.copyFromBean(new Comment(), bean);
    }

    @Override
    protected Comment copyFromBean(Comment exEntity, CommentBean entityBean) {

        if (Objects.isNull(entityBean) || Objects.isNull(exEntity)) {
            return exEntity;
        }

        if (Objects.nonNull(entityBean.getComment())) {
            exEntity.setComment(entityBean.getComment());
        }

        return exEntity;
    }

    @Override
    @Transactional
    public int add(final int issueId, final int commenterId, final CommentBean commentBean) {

        // SanityChecks
        Verify.notNull(commentBean, "$add :: commentBean must be non NULL");

        final Issue issue = super.readEntity(Issue.class, issueId, true);
        final User commenter = super.readEntity(User.class, commenterId, true);

        final Comment comment = this.createFromBean(commentBean);

        comment.setIssue(issue);
        comment.setCommenter(commenter);

        final int commentId = this.dao.save(comment);

        LOGGER.info("$add :: Successfully added comment with Id : {}, to the issue with Id : {}", commentId, issueId);
        return commentId;
    }

    @Override
    @Transactional
    public String edit(final int commentId, final CommentBean commentBean) {

        // SanityChecks
        Verify.notNull(commentBean, "$edit :: commentBean must be non NULL");

        super.update(commentId, commentBean);

        final String msg = String.format("$edit :: Successfully updated Comment with Id : %s", commentId);
        LOGGER.info(msg);

        return msg;
    }

    @Override
    @Transactional
    public String delete(final int commentId) {

        super.purge(commentId);

        final String msg = String.format("$delete :: Successfully Deleted Comment with Id : %s", commentId);
        LOGGER.info(msg);
        return msg;
    }

    @Override
    public List<CommentDTO> getByIssueId(final int issueId) {

        final Issue issue = super.readEntity(Issue.class, issueId, true);

        final List<Comment> comments = issue.getComments();
        return this.toDTOs(comments);
    }
}
