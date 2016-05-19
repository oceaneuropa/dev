package org.origin.mgm.ws;

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

public class IndexServiceApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(IndexServiceApplication.class);
	protected BundleContext bc;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceReg;

	/**
	 * 
	 * @param bc
	 * @param contextRoot
	 */
	public IndexServiceApplication(BundleContext bc, String contextRoot) {
		this.bc = bc;
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
		this.serviceReg = this.bc.registerService(Application.class, this, props);
	}

	/**
	 * Unregister the IndexServiceApplication web service. Called when Activator is stopped.
	 */
	@Override
	public void stop() {
		System.out.println("IndexServiceApplication.stop()");

		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(IndexItemsResource.class);

		// resolvers
		classes.add(IndexServiceResolver.class);

		return classes;
	}

}