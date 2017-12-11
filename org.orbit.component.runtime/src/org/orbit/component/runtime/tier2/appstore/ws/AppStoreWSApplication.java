package org.orbit.component.runtime.tier2.appstore.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see org.nb.mgm.ws.ManagementApplication
 * 
 */
public class AppStoreWSApplication extends AbstractResourceConfigApplication {

	protected static Logger LOG = LoggerFactory.getLogger(AppStoreWSApplication.class);

	protected AppStoreService service;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public AppStoreWSApplication(final BundleContext bundleContext, final AppStoreService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(AppStoreService.class);
			}
		});
		register(AppStoreWSAppsResource.class);

		if (!isEnabled(JacksonFeature.class)) {
			register(JacksonFeature.class);
		}
		if (!isEnabled(MultiPartFeature.class)) {
			register(MultiPartFeature.class);
		}
	}

	public AppStoreService getAppStoreService() {
		return this.service;
	}

}

// @Override
// public Set<Class<?>> getClasses() {
// Set<Class<?>> classes = new HashSet<Class<?>>();
//
// // resources
// classes.add(AppStoreServiceResource.class);
// classes.add(AppStoreAppsResource.class);
//
// // resolvers
// classes.add(AppStoreServiceResolver.class);
//
// // http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
// // In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
// // Add additional features such as support for Multipart.
// classes.add(MultiPartFeature.class);
//
// return classes;
// }
