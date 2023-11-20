package com.mandark.jira.app.dto;


import java.sql.Blob;

import com.mandark.jira.app.persistence.orm.entity.Attachment;
import com.mandark.jira.app.persistence.orm.entity.Issue;
import com.mandark.jira.spi.app.EntityDTO;



public class AttachmentDTO extends EntityDTO<Attachment> {

    // Fields
    // -------------------------------------------------------------------------

    private final String file_name;

    private final Blob file_data;

    private final Issue issue;

    private final String description;

    // Constructors
    // -------------------------------------------------------------------------

    public AttachmentDTO(Attachment e) {
        super(e);
        this.file_name = e.getFileName();
        this.file_data = e.getFileData();
        this.issue = e.getIssue();
        this.description = e.getDescription();
    }


    // Getters and Setters
    // -------------------------------------------------------------------------

    public String getFile_name() {
        return file_name;
    }

    public Blob getFile_data() {
        return file_data;
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
        return "AttachmentsDTO [file_name=" + file_name + ", file_data=" + file_data + ", issue=" + issue
                + ", description=" + description + "]";
    }

}
