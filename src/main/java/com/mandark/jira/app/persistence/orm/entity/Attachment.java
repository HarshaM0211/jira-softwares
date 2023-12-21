package com.mandark.jira.app.persistence.orm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;



@Entity
@Table(name = "attachments", indexes = {@Index(columnList = "issue_id", name = "issue_id")})
public class Attachment extends JpaAuditEntity {

    // Field Lables
    // ------------------------------------------------------------------------

    public static final String PROP_FILENAME = "fileName";

    public static final String PROP_ISSUE = "issue";

    // Fields
    // ------------------------------------------------------------------------

    private String fileName;

    private byte[] fileData;

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

        if (Objects.isNull(fileName)) {
            throw new ValidationException("#validate :: fileName is BLANK");
        }

        if (Objects.isNull(fileData)) {
            throw new ValidationException("#validate :: fileData is BLANK");
        }

        if (Objects.isNull(issue)) {
            throw new ValidationException("#validate :: issue is BLANK");
        }

    }


    // Getters and Setters
    // -------------------------------------------------------------------------

    @Column(name = "file_name", nullable = false)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String file_name) {
        this.fileName = file_name;
    }

    @Lob
    @Column(name = "file_data", nullable = false)
    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] file_data) {
        this.fileData = file_data;
    }

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
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
        return "Attachments [file_name=" + fileName + ", issue=" + issue.getId() + ", description=" + description + "]";
    }



}
