package org.nb.mgm.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.nb.mgm.ws.resources.ArtifactResource;
import org.nb.mgm.ws.resources.FrameworkResource;
import org.nb.mgm.ws.resources.HomeResource;
import org.nb.mgm.ws.resources.MachineResource;
import org.nb.mgm.ws.resources.MetaSectorResource;
import org.nb.mgm.ws.resources.MetaSpaceResource;
import org.nb.mgm.ws.resources.MgmServiceResolver;
import org.origin.common.rest.server.Constants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MgmApplication extends Application {

	protected static Logger logger = LoggerFactory.getLogger(MgmApplication.class);
	protected BundleContext bundleContext;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public MgmApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;
	}

	/**
	 * Registry this MgmApplication as a web service. Called when Activator is started.
	 */
	public void start() {
		logger.debug("MgmApplication.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
	}

	/**
	 * Unregister the MgmApplication web service. Called when Activator is stopped.
	 */
	public void stop() {
		logger.debug("MgmApplication.stop()");

		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(FrameworkResource.class);
		classes.add(MachineResource.class);
		classes.add(HomeResource.class);
		classes.add(MetaSectorResource.class);
		classes.add(MetaSpaceResource.class);
		classes.add(ArtifactResource.class);

		// resolvers
		classes.add(MgmServiceResolver.class);

		return classes;
	}

}
