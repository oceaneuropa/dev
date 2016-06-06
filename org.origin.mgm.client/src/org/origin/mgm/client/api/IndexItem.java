package org.origin.mgm.client.api;

import java.util.Map;

public interface IndexItem {

	/**
	 * 
	 * @return
	 */
	public String getIndexProviderId();

	/**
	 * 
	 * @return
	 */
	public String getNamespace();

	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getProperties();

}
