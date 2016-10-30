package org.orbit.component.server.configregistry.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigRegistryApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(ConfigRegistryApplication.class);
	protected BundleContext bundleContext;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public ConfigRegistryApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;
	}

	/**
	 * Registry this ConfigurationRegistryApplication as a web service. Called when Activator is started.
	 */
	public void start() {
		logger.debug("ConfigurationRegistryApplication.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

	}

	/**
	 * Unregister the ConfigurationRegistryApplication web service. Called when Activator is stopped.
	 */
	public void stop() {
		logger.debug("ConfigurationRegistryApplication.stop()");

		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(ConfigRegistryResource.class);

		// resolvers
		classes.add(ConfigRegistryServiceResolver.class);

		return classes;
	}

}
