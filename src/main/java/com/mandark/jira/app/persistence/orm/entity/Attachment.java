package com.mandark.jira.app.persistence.orm.entity;

import java.sql.Blob;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;



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


    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        super.validate();

        if (Objects.isNull(fileData)) {
            throw new ValidationException("#validate :: fileData is BLANK");
        }

        if (Objects.isNull(issue)) {
            throw new ValidationException("#validate :: issue is BLANK");
        }

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