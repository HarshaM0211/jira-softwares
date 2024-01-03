package com.mandark.jira.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mandark.jira.app.dto.AttachmentDTO;
import com.mandark.jira.app.persistence.orm.entity.Attachment;
import com.mandark.jira.spi.app.service.EntityService;


public interface AttachmentService extends EntityService<Integer, Attachment, AttachmentDTO> {

    int attach(final int issueId, final String description, final MultipartFile file);

    String delete(final List<Integer> attachmentIds);

    AttachmentDTO get(final int issueId, final String fileName);
}
