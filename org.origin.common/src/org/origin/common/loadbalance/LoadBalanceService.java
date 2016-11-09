package org.origin.common.loadbalance;

import java.util.Map;

public interface LoadBalanceService<S> {

	/**
	 * 
	 * @param service
	 */
	public void setService(S service);

	/**
	 * 
	 * @return
	 */
	public S getService();

	/**
	 * 
	 * @param id
	 */
	public void setId(String id);

	/**
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getProperties();

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasProperty(String key);

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value);

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getProperty(String key);

}
