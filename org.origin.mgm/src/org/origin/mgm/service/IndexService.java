package org.origin.mgm.service;

import java.util.List;
import java.util.Map;

import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.query.IndexItemQuery;
import org.origin.mgm.model.runtime.IndexItem;

/**
 * ServiceRegistry Service
 * 
 */
public interface IndexService {

	/**
	 * Get a list of index items.
	 * 
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItems() throws IndexServiceException;

	/**
	 * Get a list of index items by type.
	 * 
	 * @param type
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItems(String type) throws IndexServiceException;

	/**
	 * Get a list of index items by query.
	 * 
	 * @param indexItemQuery
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItems(IndexItemQuery indexItemQuery) throws IndexServiceException;

	/**
	 * Check whether an item is indexed.
	 * 
	 * @param type
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean isIndexed(String type, String name) throws IndexServiceException;

	/**
	 * Create an index item.
	 * 
	 * @param type
	 * @param name
	 * @param props
	 *            e.g. description, url
	 * @throws IndexServiceException
	 */
	public void createIndexItem(String type, String name, Map<String, Object> props) throws IndexServiceException;

	/**
	 * Remove an index item.
	 * 
	 * @param type
	 * @param name
	 * @throws IndexServiceException
	 */
	public void removeIndexItem(String type, String name) throws IndexServiceException;

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
