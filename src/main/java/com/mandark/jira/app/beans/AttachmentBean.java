package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.Attachment;
import com.mandark.jira.spi.app.EntityBean;


public class AttachmentBean implements EntityBean<Attachment> {

    private String fileName;

    private byte[] fileData;

    private String description;

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
