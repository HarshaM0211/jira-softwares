package com.mandark.jira.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mandark.jira.app.beans.CommentBean;
import com.mandark.jira.app.dto.CommentDTO;
import com.mandark.jira.app.service.CommentService;
import com.mandark.jira.spi.web.Responses;


@RestController
@RequestMapping(value = "/api/v1/issues/{issueId}/comments")
public class CommentAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentAPI.class);

    private CommentService commentService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> add(@PathVariable("issueId") int issueId, @RequestParam(required = true) int commenterId,
            @RequestBody CommentBean commentBean) {

        final int commentId = commentService.add(issueId, commenterId, commentBean);

        final String msg = String.format("$API :: Successfully added a comment with Id : %s to issue with Id : %s",
                commentId, issueId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "{commentId}", method = RequestMethod.POST)
    public ResponseEntity<?> edit(@PathVariable("commentId") int commentId, @RequestBody CommentBean commentBean) {

        final String msg = commentService.edit(commentId, commentBean);

        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("commentId") int commentId) {

        final String msg = commentService.delete(commentId);

        LOGGER.info(msg);
        return Responses.ok(msg);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getbyIssueId(@PathVariable("issueId") int issueId) {

        final List<CommentDTO> commentDtos = commentService.getByIssueId(issueId);

        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    // Getters and Setters
    // ------------------------------------------------------------------------
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
}
