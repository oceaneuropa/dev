package org.origin.core.workspace;

import java.util.Map;

import org.origin.common.resource.RObject;

public interface IResourceDescription extends RObject, Cloneable {

	/**
	 * Get name of the resource.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Set name of the resource.
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Get nature ids.
	 * 
	 * @return
	 */
	public String[] getNatureIds();

	/**
	 * Set nature ids.
	 * 
	 * @param natureIds
	 */
	public void setNatureIds(String[] natureIds);

	/**
	 * Add nature to a resource.
	 * 
	 * @param natureId
	 * @return
	 */
	public boolean addNatureId(String natureId);

	/**
	 * Remove nature from a resource.
	 * 
	 * @param natureId
	 * @return
	 */
	public boolean removeNatureId(String natureId);

	/**
	 * Get resource properties.
	 * 
	 * @return
	 */
	public Map<String, Object> getProperties();

	/**
	 * Set resource properties.
	 * 
	 * @param properties
	 */
	public void setProperties(Map<String, Object> properties);

	/**
	 * Set resource property.
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value);

	/**
	 * Remove resource property.
	 * 
	 * @param key
	 */
	public void removeProperty(String key);

}
