package org.orbit.component.runtime.tier1.account.ws;

import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class UserRegistryServiceIndexTimerFactory implements ServiceIndexTimerFactory<UserRegistryService> {

	@Override
	public UserRegistryServiceIndexTimer create(UserRegistryService service) {
		return new UserRegistryServiceIndexTimer(service);
	}

}
