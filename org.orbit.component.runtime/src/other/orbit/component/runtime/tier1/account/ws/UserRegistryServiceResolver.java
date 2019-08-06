package other.orbit.component.runtime.tier1.account.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.ComponentServices;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;

public class UserRegistryServiceResolver implements ContextResolver<UserRegistryService> {

	@Override
	public UserRegistryService getContext(Class<?> clazz) {
		return ComponentServices.getInstance().getUserRegistryService();
	}

}
