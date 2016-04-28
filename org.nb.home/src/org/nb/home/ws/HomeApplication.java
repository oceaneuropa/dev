package org.nb.home.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.origin.common.rest.server.AbstractApplication;
import org.origin.common.rest.server.Constants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(HomeApplication.class);
	protected BundleContext bundleContext;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public HomeApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;
	}

	/**
	 * Registry this HomeApplication as a web service. Called when Activator is started.
	 */
	public void start() {
		logger.info("HomeApplication.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
	}

	/**
	 * Unregister the HomeApplication web service. Called when Activator is stopped.
	 */
	public void stop() {
		logger.info("HomeApplication.stop()");

		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(HomeApiResource.class);
		classes.add(SectorResource.class);

		// resolvers
		classes.add(HomeServiceResolver.class);

		return classes;
	}

}
