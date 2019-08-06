package other.orbit.component.runtime.tier1.config.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.ComponentServices;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;

public class ConfigRegistryServiceResolver implements ContextResolver<ConfigRegistryServiceV0> {

	@Override
	public ConfigRegistryServiceV0 getContext(Class<?> clazz) {
		return ComponentServices.getInstance().getConfigRegistryService();
	}

}
