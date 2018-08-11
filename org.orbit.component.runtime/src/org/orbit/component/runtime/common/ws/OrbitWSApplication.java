package org.orbit.component.runtime.common.ws;

import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.service.WebServiceAware;

public class OrbitWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param webServiceAware
	 * @param feature
	 */
	public OrbitWSApplication(WebServiceAware webServiceAware, int feature) {
		super(webServiceAware, feature);

		if (hasFeature(OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER)) {
			register(OrbitAuthTokenRequestFilter.class);
		}

		if (hasFeature(OrbitFeatureConstants.SET_COOKIE_RESPONSE_FILTER)) {
			register(OrbitSetCookieResponseFilter.class);
		}
	}

}

// @Override
// protected int checkFeature(int feature) {
// feature = super.checkFeature(feature);
//
// if ((feature & FeatureConstants.JACKSON) == 0) {
// feature = feature | FeatureConstants.JACKSON;
// }
// if ((feature & FeatureConstants.MULTIPLEPART) == 0) {
// feature = feature | FeatureConstants.MULTIPLEPART;
// }
// return feature;
// }
