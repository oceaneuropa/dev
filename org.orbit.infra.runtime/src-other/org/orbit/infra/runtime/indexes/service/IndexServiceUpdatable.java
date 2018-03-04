package org.orbit.infra.runtime.indexes.service;

import java.util.Date;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexServiceException;
import org.orbit.infra.runtime.indexes.service.IndexService;

public interface IndexServiceUpdatable extends IndexService {

	/**
	 * Add a index item to the cached index items list.
	 * 
	 * @param indexItem
	 */
	public void addCachedIndexItem(IndexItem indexItem) throws IndexServiceException;

	/**
	 * Remove a index item from the cached index items list.
	 * 
	 * @param indexItemId
	 */
	public void removeCachedIndexItem(Integer indexItemId) throws IndexServiceException;

	/**
	 * Update the properties and last update time of a index item.
	 * 
	 * @param indexItemId
	 * @param properties
	 * @param lastUpdateTime
	 */
	public void updateCachedIndexItemProperties(Integer indexItemId, Map<String, Object> properties, Date lastUpdateTime) throws IndexServiceException;

}
