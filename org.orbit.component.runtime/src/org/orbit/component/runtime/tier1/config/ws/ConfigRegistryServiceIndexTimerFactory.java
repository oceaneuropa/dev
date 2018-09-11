package org.orbit.component.runtime.tier1.config.ws;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class ConfigRegistryServiceIndexTimerFactory implements ServiceIndexTimerFactory<ConfigRegistryService> {

	@Override
	public ConfigRegistryServiceIndexTimer create(IndexServiceClient indexProvider, ConfigRegistryService service) {
		return new ConfigRegistryServiceIndexTimer(indexProvider, service);
	}

}
