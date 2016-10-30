package org.orbit.component.connector.configregistry;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.configregistry.ConfigurationRegistry;
import org.orbit.component.api.configregistry.ConfigurationRegistryService;

public class ConfigurationServiceImpl implements ConfigurationRegistryService {

	Map<String, ConfigurationRegistry> registryMap = new HashMap<String, ConfigurationRegistry>();

	@Override
	public synchronized ConfigurationRegistry getConfigRegistry(String userId) {
		ConfigurationRegistry configRegistry = this.registryMap.get(userId);
		if (configRegistry == null) {
			configRegistry = new ConfigurationRegistryImpl(userId);
			this.registryMap.put(userId, configRegistry);
		}
		return configRegistry;
	}

}
