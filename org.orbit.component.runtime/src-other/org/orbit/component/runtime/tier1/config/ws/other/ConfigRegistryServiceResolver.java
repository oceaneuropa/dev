package org.orbit.component.runtime.tier1.config.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;

public class ConfigRegistryServiceResolver implements ContextResolver<ConfigRegistryService> {

	@Override
	public ConfigRegistryService getContext(Class<?> clazz) {
		return OrbitServices.getInstance().getConfigRegistryService();
	}

}
