package org.origin.mgm.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(IndexServiceApplication.class);
	protected BundleContext bundleContext;
	protected String contextRoot;

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public IndexServiceApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;
	}

	/**
	 * Registry this IndexServiceApplication as a web service. Called when Activator is started.
	 */
	@Override
	public void start() {
		System.out.println("IndexServiceApplication.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, contextRoot);
		OSGiServiceUtil.register(this.bundleContext, Application.class, this, props);
	}

	/**
	 * Unregister the IndexServiceApplication web service. Called when Activator is stopped.
	 */
	@Override
	public void stop() {
		System.out.println("IndexServiceApplication.stop()");

		OSGiServiceUtil.unregister(Application.class, this);
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(IndexItemsResource.class);
		classes.add(IndexItemsActionResource.class);

		// resolvers
		classes.add(IndexServiceResolver.class);

		return classes;
	}

}
