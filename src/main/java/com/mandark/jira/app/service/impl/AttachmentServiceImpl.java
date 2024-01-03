package com.mandark.jira.app.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.mandark.jira.app.beans.AttachmentBean;
import com.mandark.jira.app.dto.AttachmentDTO;
import com.mandark.jira.app.persistence.orm.entity.Attachment;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.app.service.AttachmentService;
import com.mandark.jira.spi.app.persistence.IDao;
import com.mandark.jira.spi.app.query.Criteria;
import com.mandark.jira.spi.app.service.AbstractJpaEntityService;
import com.mandark.jira.spi.util.Verify;


public class AttachmentServiceImpl extends AbstractJpaEntityService<Attachment, AttachmentBean, AttachmentDTO>
        implements AttachmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    public AttachmentServiceImpl(IDao<Integer> dao) {
        super(dao);
    }

    @Override
    protected Class<Attachment> getEntityClass() {
        return Attachment.class;
    }

    @Override
    protected AttachmentDTO toDTO(Attachment entityObj) {
        return Objects.isNull(entityObj) ? null : new AttachmentDTO(entityObj);
    }

    @Override
    protected Attachment createFromBean(AttachmentBean bean) {
        return this.copyFromBean(new Attachment(), bean);
    }

    @Override
    protected Attachment copyFromBean(Attachment exEntity, AttachmentBean entityBean) {

        if (Objects.isNull(exEntity) || Objects.isNull(entityBean)) {
            return exEntity;
        }

        if (Objects.nonNull(entityBean.getFileName())) {
            exEntity.setFileName(entityBean.getFileName());
        }

        if (Objects.nonNull(entityBean.getFileData())) {
            exEntity.setFileData(entityBean.getFileData());
        }

        if (Objects.nonNull(entityBean.getDescription())) {
            exEntity.setDescription(entityBean.getDescription());
        }
        return exEntity;
    }

    @Override
    @Transactional
    public int attach(final int issueId, final String description, final MultipartFile file) {

        Attachment attachment = new Attachment();

        final String fileName = file.getOriginalFilename();
        attachment.setFileName(fileName);
        try {
            final byte[] fileData = file.getBytes();
            attachment.setFileData(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Issue issue = super.readEntity(Issue.class, issueId, true);
        attachment.setIssue(issue);
        attachment.setDescription(description);

        final int attachmentId = this.dao.save(attachment);

        LOGGER.info("Successfully attached an attachment with Id : {}, to the issue of Id : {}", attachmentId, issueId);
        return attachmentId;
    }

    @Override
    @Transactional
    public String delete(final List<Integer> attachmentIds) {

        // Sanity Checks
        Verify.notEmpty(attachmentIds);

        super.purge(attachmentIds);

        final String msg = String.format("#delete :: Successfully Deleted attachments with Ids : %s", attachmentIds);

        LOGGER.info(msg);
        return msg;
    }

    @Override
    public AttachmentDTO get(final int issueId, final String fileName) {

        // Sanity Checks
        Verify.notNull(fileName, "$getByIssueId :: fileName must be non NULL");

        final Issue issue = super.readEntity(Issue.class, issueId, true);

        final Criteria issueCr = Criteria.equal(Attachment.PROP_ISSUE, issue);
        final Criteria fileNameCr = Criteria.equal(Attachment.PROP_FILENAME, fileName);
        final Criteria issueAndFileNameCr = Criteria.and(issueCr, fileNameCr);

        final Attachment attachment = this.dao.findOne(this.getEntityClass(), issueAndFileNameCr);

        return this.toDTO(attachment);
    }

}
