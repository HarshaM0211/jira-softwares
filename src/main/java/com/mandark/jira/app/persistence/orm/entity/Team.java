package com.mandark.jira.app.persistence.orm.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;



@Entity
@Table(name = "teams")
public class Team extends JpaAuditEntity {

    private String name;

    private Organisation organisation;

    private User teamLeader; // (org_mem_id)

    private List<User> users;

    // Constructors
    // ------------------------------------------------------------------------

    public Team() {
        super();
        // TODO Auto-generated constructor stub
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        super.validate();

        if (Objects.isNull(organisation)) {
            throw new ValidationException("#validate :: organisation is BLANK");
        }

        if (Objects.isNull(name)) {
            throw new ValidationException("#validate :: name is BLANK");
        }

    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "org_id")
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

    @ManyToMany
    @JoinColumn(name = "user_id")
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> org_members) {
        this.users = org_members;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Teams [name=" + name + ", organisation=" + organisation + ", team_leader=" + teamLeader
                + ", org_members=" + users + "]";
    }



}