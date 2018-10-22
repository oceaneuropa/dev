package org.orbit.infra.runtime.configregistry.ws;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;

public class ConfigRegistryServiceIndexTimerFactory implements ServiceIndexTimerFactory<ConfigRegistryService> {

	@Override
	public ConfigRegistryServiceIndexTimer create(IndexServiceClient indexService, ConfigRegistryService service) {
		return new ConfigRegistryServiceIndexTimer(indexService, service);
	}

}
