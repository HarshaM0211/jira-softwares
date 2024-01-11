package com.mandark.jira.app.persistence.orm.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mandark.jira.app.persistence.orm.JpaAuditEntity;
import com.mandark.jira.spi.lang.ValidationException;


@Entity
@Table(name = "team_users",
        indexes = {@Index(columnList = "team_id", name = "team_id"), @Index(columnList = "user_id", name = "user_id")})
public class TeamMember extends JpaAuditEntity {

    // Field Lables
    // ------------------------------------------------------------------------

    public static final String PROP_TEAM = "team";

    public static final String PROP_USER = "user";

    // Fields
    // ------------------------------------------------------------------------

    private Team team;

    private User user;

    // Constructor
    // ------------------------------------------------------------------------

    public TeamMember() {
        super();
    }

    // Validatable
    // ------------------------------------------------------------------------

    @Override
    public void validate() {

        if (Objects.isNull(team)) {
            throw new ValidationException("#validate :: team is BLANK");
        }

        if (Objects.isNull(user)) {
            throw new ValidationException("#validate :: user is BLANK");
        }
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "TeamMember [team=" + team + ", user=" + user + "]";
    }
}
