package org.orbit.component.server.configregistry.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.server.Activator;
import org.orbit.component.server.configregistry.service.ConfigRegistryService;

public class ConfigRegistryServiceResolver implements ContextResolver<ConfigRegistryService> {

	@Override
	public ConfigRegistryService getContext(Class<?> clazz) {
		return Activator.getConfigRegistryService();
	}

}
