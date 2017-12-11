package org.orbit.component.runtime.tier1.session.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.tier1.session.service.OAuth2Service;

public class OAuth2ServiceResolver implements ContextResolver<OAuth2Service> {

	@Override
	public OAuth2Service getContext(Class<?> clazz) {
		// return Activator.getOAuth2Service();
		return null;
	}

}
