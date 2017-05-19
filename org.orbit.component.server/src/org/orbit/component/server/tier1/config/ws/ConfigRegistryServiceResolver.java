package org.orbit.component.server.tier1.config.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.server.Activator;
import org.orbit.component.server.tier1.config.service.ConfigRegistryService;

public class ConfigRegistryServiceResolver implements ContextResolver<ConfigRegistryService> {

	@Override
	public ConfigRegistryService getContext(Class<?> clazz) {
		return Activator.getConfigRegistryService();
	}

}
