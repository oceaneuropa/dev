package com.osgi.example1.fs.server.ws;

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

public class FileSystemApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(FileSystemApplication.class);
	protected BundleContext bundleContext;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public FileSystemApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;
	}

	@Override
	public void start() {
		logger.debug("FileSystemApplication.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
	}

	@Override
	public void stop() {
		logger.debug("FileSystemApplication.stop()");

		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(FileSystemResource.class);

		// resolvers
		classes.add(FileSystemResolver.class);

		return classes;
	}

}
