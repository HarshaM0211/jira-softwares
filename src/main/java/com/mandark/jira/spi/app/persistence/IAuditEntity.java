package com.mandark.jira.spi.app.persistence;

import java.time.LocalDateTime;


/**
 * Definition of any Entity that can be persisted to DataStore / DataBase.
 * 
 * <p>
 * All the implementations/entities of this definition will provide the below information.
 * </p>
 * 
 * <ul>
 * <li>id - A unique identifier of the object.</li>
 * <li>version - version number object.</li>
 * <li>createdBy - Info of user who created</li>
 * <li>createdOn - Created timestamp</li>
 * <li>updatedBy - Info of user who last updated</li>
 * <li>updatedOn - Last Updated timestamp</li>
 * </ul>
 *
 * @param <K> Type Parameter of the Entity Identifier
 */
public interface IAuditEntity<K> extends IEntity<K> {

    // Version

    /**
     * Returns the current version/revision of the object.
     * 
     * @return the version number
     */
    Integer getVersion();

    /**
     * Sets the current version/revision of the object.
     * <p>
     * <b>Note :</b> This is supposed to be set by the Entity Manager in the persistence context. The
     * values set may possibly disturb the version management.
     * </p>
     * 
     * @param version version of the object
     */
    void setVersion(Integer version);



    // Editor Info :: Create

    /**
     * Get the info of User who created this record.
     * 
     * @return Unique username/id of the User
     */
    String getCreatedBy();

    /**
     * Set the info of User who created this record.
     * 
     * @param createdBy Unique username/id of the User
     */
    void setCreatedBy(String createdBy);

    /**
     * Returns when the object is created
     * 
     * @return the created on timestamp
     */
    LocalDateTime getCreatedOn();

    /**
     * Sets the created timestamp
     * 
     * <p>
     * <b>Note :</b> This is supposed to be set by the persistent listener. The values set may possibly
     * get overridden by the interceptor.
     * </p>
     * 
     * @param createdOn the created on date
     */
    void setCreatedOn(LocalDateTime createdOn);



    // Editor Info :: Update

    /**
     * Get the info of User who last updated this record.
     * 
     * @return Unique username/id of the User
     */
    String getUpdatedBy();

    /**
     * Set the info of User who last updated this record.
     * 
     * @param updatedBy Unique username/id of the User
     */
    void setUpdatedBy(String updatedBy);


    /**
     * Returns when the object is last updated
     * 
     * @return the last updated on timestamp
     */
    LocalDateTime getUpdatedOn();

    /**
     * Sets the last updated timestamp
     * 
     * <p>
     * <b>Note :</b> This is supposed to be set by the persistent listener. The values set may possibly
     * get overridden by the interceptor.
     * </p>
     * 
     * @param updatedOn the last updated on date
     */
    void setUpdatedOn(LocalDateTime updatedOn);



}
