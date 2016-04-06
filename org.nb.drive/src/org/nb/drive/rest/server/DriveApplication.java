package org.nb.drive.rest.server;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DriveApplication extends Application {

	public static final String CONTEXT_ROOT = "context_root";

	protected BundleContext bundleContext;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public DriveApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;
	}

	/**
	 * Registry this DriveApplication as a web service. Called when Activator is started.
	 */
	public void start() {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(CONTEXT_ROOT, contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
	}

	/**
	 * Unregister the DriveApplication web service. Called when Activator is stopped.
	 */
	public void stop() {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(DriveResource.class);

		// resolvers
		classes.add(DriveServiceResolver.class);

		return classes;
	}

}
