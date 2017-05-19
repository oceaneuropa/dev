package org.orbit.component.server.tier1.session.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.server.Activator;
import org.orbit.component.server.tier1.session.service.OAuth2Service;

public class OAuth2ServiceResolver implements ContextResolver<OAuth2Service> {

	@Override
	public OAuth2Service getContext(Class<?> clazz) {
		return Activator.getOAuth2Service();
	}

}
