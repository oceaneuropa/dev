package org.orbit.component.runtime.tier1.account.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.Activator;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;

public class UserRegistryServiceResolver implements ContextResolver<UserRegistryService> {

	@Override
	public UserRegistryService getContext(Class<?> clazz) {
		return Activator.getInstance().getUserRegistryService();
	}

}