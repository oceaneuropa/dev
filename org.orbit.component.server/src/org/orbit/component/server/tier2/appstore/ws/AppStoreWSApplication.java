package org.orbit.component.server.tier2.appstore.ws;

import java.util.Hashtable;

import javax.ws.rs.core.Application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @see org.nb.mgm.ws.ManagementApplication
 * 
 */
public class AppStoreWSApplication extends AbstractResourceConfigApplication {

	protected AppStoreService service;
	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected AppStoreServiceIndexTimerV2 serviceIndexTimer;

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
		register(AppStoreServiceResource.class);
		register(AppStoreAppsResource.class);

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

	public IndexProvider getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	@Override
	public void start() {
		// System.out.println(getClass().getSimpleName() + ".start()");
		if (this.isStarted.get()) {
			return;
		}
		super.start();
		this.isStarted.set(true);

		// Register Application service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start timer for indexing the service
		// this.serviceIndexTimer = new AppStoreServiceIndexTimer(this.indexProvider);
		this.serviceIndexTimer = new AppStoreServiceIndexTimerV2(this.indexProvider, this.service);
		this.serviceIndexTimer.start();

		System.out.println(getClass().getSimpleName() + ".start(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is started.");
	}

	@Override
	public void stop() {
		// System.out.println(getClass().getSimpleName() + ".stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// Start timer for indexing the service
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Unregister Application service
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		super.stop();

		System.out.println(getClass().getSimpleName() + ".stop(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is stopped.");
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
