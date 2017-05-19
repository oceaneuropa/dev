package org.origin.mgm.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.mgm.Activator;
import org.origin.mgm.service.IndexServiceIndexTimer;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(IndexServiceApplication.class);

	protected BundleContext bundleContext;
	protected String contextRoot;
	protected IndexServiceIndexTimer serviceIndexTimer;

	public IndexServiceApplication() {
	}

	public BundleContext getBundleContext() {
		return this.bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	@Override
	public void start() {
		System.out.println(getClass().getSimpleName() + ".start()");

		super.start();

		// Register web application
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, contextRoot);
		OSGiServiceUtil.register(this.bundleContext, Application.class, this, props);

		// Start timer for indexing the service
		this.serviceIndexTimer = new IndexServiceIndexTimer(Activator.getIndexService());
		this.serviceIndexTimer.start();
	}

	@Override
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");

		// Stop timer for indexing the service
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Unregister web application
		OSGiServiceUtil.unregister(Application.class, this);

		super.stop();
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(IndexServiceResource.class);
		classes.add(IndexItemsResource.class);
		classes.add(IndexItemResource.class);

		// resolvers
		classes.add(IndexServiceResolver.class);

		return classes;
	}

}
