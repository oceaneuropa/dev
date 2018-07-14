package org.orbit.component.runtime.tier1.auth.ws;

import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class AuthServiceIndexTimerFactory implements ServiceIndexTimerFactory<AuthService> {

	@Override
	public AuthServiceIndexTimer create(IndexProvider indexProvider, AuthService service) {
		return new AuthServiceIndexTimer(indexProvider, service);
	}

}
