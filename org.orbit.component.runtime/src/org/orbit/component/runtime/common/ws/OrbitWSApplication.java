package org.orbit.component.runtime.common.ws;

import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class OrbitWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public OrbitWSApplication(String contextRoot, int feature) {
		super(contextRoot, feature);

		if (hasFeature(OrbitFeatureConstants.AUTHORIZATION_TOKEN_REQUEST_FILTER)) {
			register(AuthorizationTokenRequestFilter.class);
		}

		if (hasFeature(OrbitFeatureConstants.SET_COOKIE_RESPONSE_FILTER)) {
			register(SetCookieResponseFilter.class);
		}
	}

}
