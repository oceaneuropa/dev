package org.orbit.component.runtime.tier1.config.service;

public interface ConfigRegistryService {

	String getNamespace();

	String getName();

	String getHostURL();

	String getContextRoot();

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public ConfigRegistry getRegistry(String userId);

}
