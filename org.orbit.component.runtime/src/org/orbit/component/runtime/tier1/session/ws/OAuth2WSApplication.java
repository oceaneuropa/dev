package org.orbit.component.runtime.tier1.session.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OAuth2WSApplication extends AbstractResourceConfigApplication /* AbstractApplication */ {

	protected static Logger logger = LoggerFactory.getLogger(OAuth2WSApplication.class);

	protected OAuth2Service service;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public OAuth2WSApplication(final BundleContext bundleContext, final OAuth2Service service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(OAuth2Service.class);
			}
		});
		// register(OAuth2ServiceWSResource.class);
	}

}

// @Override
// public Set<Class<?>> getClasses() {
// Set<Class<?>> classes = new HashSet<Class<?>>();
//
// // resources
// classes.add(OAuth2ServiceResource.class);
//
// // resolvers
// classes.add(OAuth2ServiceResolver.class);
//
// return classes;
// }

// protected OAuth2ServiceIndexTimer serviceIndexTimer;

// protected ServiceRegistration<?> serviceRegistration;
// @Override
// public void start() {
// // System.out.println(getClass().getSimpleName() + ".start()");
// if (this.isStarted.get()) {
// return;
// }
// super.start();
// this.isStarted.set(true);
//
// // Register Application service
// Hashtable<String, Object> props = new Hashtable<String, Object>();
// props.put(Constants.CONTEXT_ROOT, this.contextRoot);
// this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
//
// System.out.println(getClass().getSimpleName() + ".start(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is
// started.");
// }
//
// @Override
// public void stop() {
// System.out.println(getClass().getSimpleName() + ".stop()");
// if (!this.isStarted.compareAndSet(true, false)) {
// return;
// }
//
// // Unregister Application service
// if (this.serviceRegistration != null) {
// this.serviceRegistration.unregister();
// this.serviceRegistration = null;
// }
//
// super.stop();
//
// System.out.println(getClass().getSimpleName() + ".stop(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is
// stopped.");
// }