package org.orbit.component.server.tier1.config.service;

public interface ConfigRegistryService {

	/**
	 * 
	 * @return
	 */
	public String getHostURL();

	/**
	 * 
	 * @return
	 */
	public String getContextRoot();

	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public ConfigRegistry getRegistry(String userId);

}
