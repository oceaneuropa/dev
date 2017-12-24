package org.orbit.component.runtime.tier1.config.service;

public interface ConfigRegistryService {

	String getName();

	String getHostURL();

	String getContextRoot();

	ConfigRegistry getRegistry(String userId);

}
