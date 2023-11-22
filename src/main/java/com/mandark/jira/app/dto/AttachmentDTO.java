package com.mandark.jira.app.dto;


import com.mandark.jira.app.persistence.orm.entity.Attachment;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.EntityDTO;



public class AttachmentDTO extends EntityDTO<Attachment> {

    // Fields
    // -------------------------------------------------------------------------

    private final String fileName;

    private final byte[] fileData;

    private final Issue issue;

    private final String description;

    // Constructors
    // -------------------------------------------------------------------------

    public AttachmentDTO(Attachment e) {
        super(e);
        this.fileName = e.getFileName();
        this.fileData = e.getFileData();
        this.issue = e.getIssue();
        this.description = e.getDescription();
    }

    // Getters and Setters
    // -------------------------------------------------------------------------

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public Issue getIssue() {
        return issue;
    }

    public String getDescription() {
        return description;
    }

    // Object Methods
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "AttachmentsDTO [fileName=" + fileName + ", issue=" + issue + ", description=" + description + "]";
    }

}
