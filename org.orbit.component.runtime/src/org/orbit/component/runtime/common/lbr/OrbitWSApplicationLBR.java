package org.orbit.component.runtime.common.lbr;

import org.orbit.component.runtime.common.ws.AuthorizationTokenRequestFilter;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.common.ws.SetCookieResponseFilter;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class OrbitWSApplicationLBR extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public OrbitWSApplicationLBR(String contextRoot, int feature) {
		super(contextRoot, feature);

		if (hasFeature(OrbitFeatureConstants.AUTHORIZATION_TOKEN_REQUEST_FILTER)) {
			register(AuthorizationTokenRequestFilter.class);
		}

		if (hasFeature(OrbitFeatureConstants.SET_COOKIE_RESPONSE_FILTER)) {
			register(SetCookieResponseFilter.class);
		}
	}

}
