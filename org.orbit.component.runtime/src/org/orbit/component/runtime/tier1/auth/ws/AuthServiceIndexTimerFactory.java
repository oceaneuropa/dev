package org.orbit.component.runtime.tier1.auth.ws;

import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class AuthServiceIndexTimerFactory implements ServiceIndexTimerFactory<AuthService> {

	@Override
	public AuthServiceIndexTimer create(IndexServiceClient indexProvider, AuthService service) {
		return new AuthServiceIndexTimer(indexProvider, service);
	}

}
