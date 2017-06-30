package org.origin.common.loadbalance;

import java.util.Map;

import org.origin.common.adapter.IAdaptable;

public interface LoadBalanceResource<S> extends IAdaptable {

	/**
	 * 
	 * @param service
	 */
	void setService(S service);

	/**
	 * 
	 * @return
	 */
	S getService();

	/**
	 * 
	 * @param id
	 */
	void setId(String id);

	/**
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * 
	 * @param key
	 * @return
	 */
	boolean hasProperty(String key);

	/**
	 * 
	 * @param properties
	 */
	void setProperties(Map<String, Object> properties);

	/**
	 * 
	 * @param key
	 * @param value
	 */
	void setProperty(String key, Object value);

	/**
	 * 
	 * @param key
	 * @return
	 */
	Object removeProperty(String key);

	/**
	 * 
	 * @param key
	 * @return
	 */
	Object getProperty(String key);

}
