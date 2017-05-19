package org.orbit.component.server.tier1.account.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.server.Activator;
import org.orbit.component.server.tier1.account.service.UserRegistryService;

public class UserRegistryServiceResolver implements ContextResolver<UserRegistryService> {

	@Override
	public UserRegistryService getContext(Class<?> clazz) {
		return Activator.getUserRegistryService();
	}

}
