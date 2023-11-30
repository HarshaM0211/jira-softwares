package com.mandark.jira.app.persistence.orm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;



@Entity
@Table(name = "teams", indexes = {@Index(columnList = "org_id", name = "org_id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "org_id"})})
public class Team extends JpaAuditEntity {

    private String name;

    private Organisation organisation;

    private User teamLeader; // (user_id)

    // Constructors
    // ------------------------------------------------------------------------

    public Team() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(organisation)) {
            throw new ValidationException("#validate :: organisation is BLANK");
        }

        if (Objects.isNull(name)) {
            throw new ValidationException("#validate :: name is BLANK");
        }

    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    @OneToOne
    @JoinColumn(name = "team_leader_id")
    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User team_leader) {
        this.teamLeader = team_leader;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Teams [name=" + name + ", organisation=" + organisation.getId() + ", team_leader=" + teamLeader + "]";
    }



}
