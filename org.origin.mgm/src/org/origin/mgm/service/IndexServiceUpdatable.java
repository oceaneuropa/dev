package org.origin.mgm.service;

import java.util.Date;
import java.util.Map;

import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;

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
	public void udpateCachedIndexItem(Integer indexItemId, Map<String, Object> properties, Date lastUpdateTime) throws IndexServiceException;

}
