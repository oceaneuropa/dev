package org.orbit.component.server.configregistry.service;

public interface ConfigRegistryService {

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public ConfigRegistry getRegistry(String userId);

}
