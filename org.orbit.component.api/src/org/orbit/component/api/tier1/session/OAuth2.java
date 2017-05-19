package org.orbit.component.api.tier1.session;

import java.util.Map;

public interface OAuth2 {

	/**
	 * Get UserRegistry name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get UserRegistry URL.
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * Get properties.
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * Ping the service.
	 * 
	 * @return
	 */
	boolean ping();

	/**
	 * Update properties.
	 * 
	 * @param properties
	 */
	void update(Map<String, Object> properties);

}
