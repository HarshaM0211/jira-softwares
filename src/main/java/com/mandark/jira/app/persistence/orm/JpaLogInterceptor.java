package com.mandark.jira.app.persistence.orm;

import java.time.LocalDateTime;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Its an interceptor to intercept the persistence layer actions. Once intercepted, some of the
 * fields in the object/entity are set/updated as needed by the action.
 * 
 * <p>
 * Intercept actions are as follows.
 * <li><b>Persist</b> : sets in the entity values for createdBy, createdOn</li>
 * <li><b>Update</b> : sets in the entity values for updatedBy, updatedOn</li>
 * <p>
 */
public class JpaLogInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaLogInterceptor.class);

    // Callback Methods
    // ------------------------------------------------------------------------

    /**
     * This intercept method is executed during the object persist.
     * 
     * @param entity the entity being persisted
     */

    @PrePersist
    void onPreCreate(Object entity) {
        // Ignore JpaEntity
        if (entity instanceof JpaAuditEntity) {
            JpaAuditEntity jpaAuditEntity = (JpaAuditEntity) entity;
            jpaAuditEntity.validate();
            jpaAuditEntity.setCreatedOn(LocalDateTime.now());
            LOGGER.debug("Pre-persist jpaAuditEntity : {}", jpaAuditEntity);
            return;
        }

        JpaEntity jpaEntity = (JpaEntity) entity;
        jpaEntity.validate();
    }

    @PostPersist
    void onPostCreate(Object entity) {

    }

    /**
     * This intercept method is executed during the object update.
     * 
     * @param entity the entity being updated
     */
    @PreUpdate
    void onPreUpdate(Object entity) {
        // Ignore MetaEntity
        if (entity instanceof JpaAuditEntity) {
            JpaAuditEntity jpaAuditEntity = (JpaAuditEntity) entity;
            jpaAuditEntity.validate();
            jpaAuditEntity.setUpdatedOn(LocalDateTime.now());
            LOGGER.debug("Pre-update base entity : {}", jpaAuditEntity);
            return;
        }

        JpaEntity jpaEntity = (JpaEntity) entity;
        jpaEntity.validate();
    }
}
