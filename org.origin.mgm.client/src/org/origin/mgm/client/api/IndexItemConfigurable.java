package org.origin.mgm.client.api;

import java.util.Map;

public interface IndexItemConfigurable extends IndexItem {

	/**
	 * Update all properties of the index item.
	 * 
	 * @param properties
	 */
	public void update(Map<String, Object> properties);

	/**
	 * Set the property of an index item.
	 * 
	 * @param propName
	 * @param propValue
	 */
	public void setProperty(String propName, Object propValue);

	/**
	 * Set the property of an index item.
	 * 
	 * @param propName
	 * @param propValue
	 * @param isVolatile
	 */
	public void setProperty(String propName, Object propValue, boolean isVolatile);

	/**
	 * Remove the property of an index item
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 */
	public void removeProperty(String propName);

	/**
	 * Delete the index item.
	 */
	public void delete();

}
