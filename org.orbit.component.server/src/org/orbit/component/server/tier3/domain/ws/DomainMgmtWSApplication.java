package org.orbit.component.server.tier3.domain.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.component.server.tier3.domain.service.DomainMgmtServiceTimer;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainMgmtWSApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(DomainMgmtWSApplication.class);

	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected DomainMgmtServiceTimer serviceIndexTimer;

	public DomainMgmtWSApplication() {
	}

	public IndexProvider getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	@Override
	public void start() {
		logger.debug("DomainMgmtWSApplication.start()");
		if (this.isStarted.get()) {
			return;
		}
		super.start();
		this.isStarted.set(true);

		// Register the service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start timer for indexing the service
		this.serviceIndexTimer = new DomainMgmtServiceTimer(this.indexProvider);
		this.serviceIndexTimer.start();
	}

	@Override
	public void stop() {
		logger.debug("DomainMgmtWSApplication.stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// Start timer for indexing the service
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Unregister the service
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		super.stop();
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(DomainMgmtServiceResource.class);
		classes.add(DomainMgmtMachinesResource.class);
		classes.add(DomainMgmtTransferAgentsResource.class);

		// resolvers
		classes.add(DomainMgmtServiceResolver.class);

		// http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
		// In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
		// Add additional features such as support for Multipart.
		classes.add(MultiPartFeature.class);

		return classes;
	}

}
