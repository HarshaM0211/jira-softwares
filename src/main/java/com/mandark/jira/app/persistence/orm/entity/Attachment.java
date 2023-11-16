package com.mandark.jira.app.persistence.orm.entity;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;



@Entity
@Table(name = "attachments")
public class Attachment extends JpaAuditEntity {

    private String fileName;

    private Blob fileData;

    private Issue issue;

    private String description;

    // Constructors
    // -------------------------------------------------------------------------

    // Default
    public Attachment() {
        super();
    }

    public Attachment(String file_name, Blob file_data, Issue issue, String description) {
        super();
        this.fileName = file_name;
        this.fileData = file_data;
        this.issue = issue;
        this.description = description;
    }

    // Getters and Setters
    // -------------------------------------------------------------------------
    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String file_name) {
        this.fileName = file_name;
    }

    @Column(name = "file_data")
    public Blob getFileData() {
        return fileData;
    }

    public void setFileData(Blob file_data) {
        this.fileData = file_data;
    }

    @ManyToOne
    @JoinColumn(name = "issue_id")
    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Object Methods
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Attachments [file_name=" + fileName + ", file_data=" + fileData + ", issue=" + issue + ", description="
                + description + "]";
    }



}
