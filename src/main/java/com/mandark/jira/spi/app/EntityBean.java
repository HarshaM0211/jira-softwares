package com.mandark.jira.spi.app;

import com.mandark.jira.spi.app.persistence.IEntity;


/**
 * Marker Interface for incoming entity Bean definitions.
 * 
 * @param <E> corresponding {@link IEntity} of the bean
 */
public interface EntityBean<E extends IEntity<?>> {

}
