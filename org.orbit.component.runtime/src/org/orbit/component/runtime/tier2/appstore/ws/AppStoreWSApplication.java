package org.orbit.component.runtime.tier2.appstore.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.origin.common.rest.server.FeatureConstants;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class AppStoreWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public AppStoreWSApplication(AppStoreService service, int feature) {
		super(service, feature);
		// adapt(AppStoreService.class, service);

		registerService(AppStoreService.class, service);
		registerResource(AppStoreWSAppsResource.class);
		registerResource(AppStoreWSAppResource.class);
		registerResource(AppStoreWSContentResource.class);
	}

	@Override
	protected boolean hasFeature(int targetFeature) {
		if (OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER == targetFeature) {
			return false;
		}
		if ((this.feature & targetFeature) == targetFeature) {
			return true;
		}
		return false;
	}

	@Override
	protected int checkFeature(int feature) {
		feature = super.checkFeature(feature);

		if ((feature & FeatureConstants.JACKSON) == 0) {
			feature = feature | FeatureConstants.JACKSON;
		}
		if ((feature & FeatureConstants.MULTIPLEPART) == 0) {
			feature = feature | FeatureConstants.MULTIPLEPART;
		}
		return feature;
	}

}

/*-
register(new AbstractBinder() {
	@Override
	protected void configure() {
		bind(service).to(AppStoreService.class);
	}
});
register(AppStoreWSAppsResource.class);
register(AppStoreWSAppResource.class);
register(AppStoreWSContentResource.class);
*/
