package com.mandark.jira.spi.app.persistence;


/**
 * Definition of any Entity that can be persisted to DataStore / DataBase.
 * 
 * <p>
 * All the implementations/entities of this definition will provide the below information.
 * </p>
 * 
 * <ul>
 * <li>id - A unique identifier of the object.</li>
 * </ul>
 *
 * @param <K> Type Parameter of the Entity Identifier
 */
public interface IEntity<K> {


    /**
     * Returns the Unique Identifier of the object.
     * 
     * @return the unique identifier of the object (for RDB - primary key )
     */
    public K getId();

    /**
     * Sets the Unique Identifier of the object.
     * 
     * @param pkey the unique identifier of the object (for RDB - primary key )
     */
    public void setId(K id);


}
