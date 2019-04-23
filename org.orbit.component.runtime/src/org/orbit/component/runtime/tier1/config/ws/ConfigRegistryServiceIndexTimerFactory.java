package org.orbit.component.runtime.tier1.config.ws;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class ConfigRegistryServiceIndexTimerFactory implements ServiceIndexTimerFactory<ConfigRegistryServiceV0> {

	@Override
	public ConfigRegistryServiceIndexTimer create(ConfigRegistryServiceV0 service) {
		return new ConfigRegistryServiceIndexTimer(service);
	}

}
