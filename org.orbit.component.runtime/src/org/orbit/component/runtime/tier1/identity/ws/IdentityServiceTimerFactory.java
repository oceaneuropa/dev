package org.orbit.component.runtime.tier1.identity.ws;

import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class IdentityServiceTimerFactory implements ServiceIndexTimerFactory<IdentityService> {

	@Override
	public IdentityServiceTimer create(IdentityService service) {
		return new IdentityServiceTimer(service);
	}

}
