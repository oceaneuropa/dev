package org.nb.mgm.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagementApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(ManagementApplication.class);
	protected BundleContext bundleContext;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public ManagementApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;
	}

	/**
	 * Registry this MgmApplication as a web service. Called when Activator is started.
	 */
	public void start() {
		logger.debug("ManagementApplication.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
	}

	/**
	 * Unregister the MgmApplication web service. Called when Activator is stopped.
	 */
	public void stop() {
		logger.debug("ManagementApplication.stop()");

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
		classes.add(MachinePropertiesResource.class);
		classes.add(HomeResource.class);
		classes.add(HomePropertiesResource.class);
		classes.add(MetaSectorResource.class);
		classes.add(MetaSpaceResource.class);
		classes.add(ArtifactResource.class);
		classes.add(ProjectResource.class);
		classes.add(ProjectHomeResource.class);
		classes.add(ProjectNodeResource.class);
		classes.add(ProjectSoftwareResource.class);
		classes.add(ProjectSoftwareContentResource.class);

		// resolvers
		classes.add(ManagementServiceResolver.class);

		// http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
		// In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
		// Add additional features such as support for Multipart.
		classes.add(MultiPartFeature.class);

		return classes;
	}

}
