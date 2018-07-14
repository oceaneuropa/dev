package org.orbit.component.runtime.tier1.session.ws;

import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class OAuth2ServiceIndexTimerFactory implements ServiceIndexTimerFactory<OAuth2Service> {

	@Override
	public OAuth2ServiceIndexTimer create(IndexProvider indexProvider, OAuth2Service service) {
		return new OAuth2ServiceIndexTimer(indexProvider, service);
	}

}
