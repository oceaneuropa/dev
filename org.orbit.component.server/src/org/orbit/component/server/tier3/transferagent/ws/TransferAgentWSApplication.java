package org.orbit.component.server.tier3.transferagent.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.component.server.tier3.transferagent.command.TransferAgentEditPolicyFactory;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.ServiceRegistration;

public class TransferAgentWSApplication extends AbstractApplication {

	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected TransferAgentServiceTimer serviceIndexTimer;

	public TransferAgentWSApplication() {
	}

	public IndexProvider getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	@Override
	public void start() {
		System.out.println(getClass().getSimpleName() + ".start()");
		if (this.isStarted.get()) {
			return;
		}
		super.start();
		this.isStarted.set(true);

		// Register TransferAgentEditPolicy for TransferAgentServiceResource
		TransferAgentEditPolicyFactory.register();

		// Register the service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start timer for indexing the service
		if (this.indexProvider != null) {
			this.serviceIndexTimer = new TransferAgentServiceTimer(this.indexProvider);
			this.serviceIndexTimer.start();
		}
	}

	@Override
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");
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

		// Unregister TransferAgentEditPolicy for TransferAgentServiceResource
		TransferAgentEditPolicyFactory.unregister();

		super.stop();
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(TransferAgentServiceResource.class);

		// resolvers
		classes.add(TransferAgentResolver.class);

		// http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
		// In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
		// Add additional features such as support for Multipart.
		classes.add(MultiPartFeature.class);

		return classes;
	}

}
