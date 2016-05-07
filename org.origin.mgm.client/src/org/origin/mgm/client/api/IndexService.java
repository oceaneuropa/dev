package org.origin.mgm.client.api;

import java.util.List;

import org.origin.common.rest.client.ClientException;
import org.origin.mgm.model.runtime.IndexItem;

public interface IndexService {

	/**
	 * 
	 * @return
	 */
	public List<IndexItem> getIndexItems() throws ClientException;

	/**
	 * Check whether an item indexed.
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public boolean isIndexed(String type, String name);

	/**
	 * Add an index item.
	 * 
	 * @param type
	 * @param name
	 */
	public void addIndexItem(String type, String name);

	/**
	 * Remove in index item.
	 * 
	 * @param type
	 * @param name
	 */
	public void removeIndexItem(String type, String name);

	/**
	 * Check whether a property of an index item is available.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 *            property name
	 * @return
	 */
	public boolean hasProperty(String type, String name, String propName);

	/**
	 * Set the property of an index item.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @param propValue
	 */
	public void setProperty(String type, String name, String propName, String propValue);

	/**
	 * Set the volatile property of an index item.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @param propValue
	 */
	public void setVolatileProperty(String type, String name, String propName, String propValue);

	/**
	 * Remove the property of an index item
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 */
	public void removeProperty(String type, String name, String propName);

}
