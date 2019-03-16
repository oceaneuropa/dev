package org.orbit.component.runtime.tier1.config.ws;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class ConfigRegistryServiceIndexTimerFactory implements ServiceIndexTimerFactory<ConfigRegistryService> {

	@Override
	public ConfigRegistryServiceIndexTimer create(ConfigRegistryService service) {
		return new ConfigRegistryServiceIndexTimer(service);
	}

}
