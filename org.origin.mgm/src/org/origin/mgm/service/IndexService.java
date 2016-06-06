package org.origin.mgm.service;

import java.util.List;
import java.util.Map;

import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;

public interface IndexService {

	/**
	 * Get the index service configuration.
	 * 
	 * @return
	 */
	public IndexServiceConfiguration getConfiguration();

	/**
	 * Get all index items with any namespace and created by any index provider.
	 * 
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItems() throws IndexServiceException;

	/**
	 * Get index items with specified indexProviderId.
	 * 
	 * @param indexProviderId
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItemsByIndexProvider(String indexProviderId) throws IndexServiceException;

	/**
	 * Get index items with specified namespace.
	 * 
	 * @param namespace
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItemsByNamespace(String namespace) throws IndexServiceException;

	/**
	 * Get index items with specified namespace and created by specified indexer provider.
	 * 
	 * @param indexProviderId
	 * @param namespace
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItems(String indexProviderId, String namespace) throws IndexServiceException;

	/**
	 * Add an index item.
	 * 
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 * @param properties
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean addIndexItem(String indexProviderId, String namespace, String name, Map<String, Object> properties) throws IndexServiceException;

	/**
	 * Remove an index item.
	 * 
	 * @param indexItemId
	 * @throws IndexServiceException
	 */
	public boolean removeIndexItem(Integer indexItemId) throws IndexServiceException;

	/**
	 * Get all properties names of an index item.
	 * 
	 * @param type
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	public String[] getPropertyNames(String type, String name) throws IndexServiceException;

	/**
	 * Get the properties of an index item.
	 * 
	 * @param type
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	public Map<String, Object> getProperties(String type, String name) throws IndexServiceException;

	/**
	 * Check whether a property of an index item is available.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean hasProperty(String namespace, String name, String propName) throws IndexServiceException;

	/**
	 * Get the property value of an index item.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @return
	 * @throws IndexServiceException
	 */
	public Object getProperty(String type, String name, String propName) throws IndexServiceException;

	/**
	 * Set the property of a service.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @param propValue
	 * @throws IndexServiceException
	 */
	public void setProperty(String type, String name, String propName, Object propValue) throws IndexServiceException;

	/**
	 * Remove the property of a service.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @throws IndexServiceException
	 */
	public void removeProperty(String type, String name, String propName) throws IndexServiceException;

	/**
	 * Add a IndexServiceListener.
	 * 
	 * @param listener
	 */
	public void addListener(IndexServiceListener listener);

	/**
	 * Remove a IndexServiceListener.
	 * 
	 * @param listener
	 */
	public void removeListener(IndexServiceListener listener);

}
