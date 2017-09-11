package org.orbit.component.server.tier3.transferagent.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.component.server.tier3.transferagent.timer.TransferAgentServiceTimerV2;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.core.resources.server.service.ResourceService;
import org.origin.core.resources.server.ws.ResourceWSApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.ServiceRegistration;

public class TransferAgentWSApplication extends AbstractApplication {

	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	// protected TransferAgentServiceTimer serviceIndexTimer;
	protected TransferAgentServiceTimerV2 serviceIndexTimer;

	protected ResourceWSApplication resourceWSApp1;
	protected ResourceWSApplication resourceWSApp2;

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
		// TransferAgentEditPolicyFactory.register();

		// Register the service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start timer for indexing the service
		if (this.indexProvider != null) {
			this.serviceIndexTimer = new TransferAgentServiceTimerV2(this.indexProvider);
			this.serviceIndexTimer.start();
		}

		String contextRoot1 = this.contextRoot + "/root1"; // e.g. /orbit/v1/ta/root1
		ResourceService resourceService1 = new ResourceService();
		resourceService1.setName("root1");
		resourceService1.setContextRoot(contextRoot1);
		this.resourceWSApp1 = new ResourceWSApplication(this.bundleContext, resourceService1);

		String contextRoot2 = this.contextRoot + "/root2"; // e.g. /orbit/v1/ta/root2
		ResourceService resourceService2 = new ResourceService();
		resourceService2.setName("root2");
		resourceService2.setContextRoot(contextRoot2);
		this.resourceWSApp2 = new ResourceWSApplication(this.bundleContext, resourceService2);

		this.resourceWSApp1.start();
		this.resourceWSApp2.start();
	}

	@Override
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		if (this.resourceWSApp1 != null) {
			this.resourceWSApp1.stop();
			this.resourceWSApp1 = null;
		}
		if (this.resourceWSApp2 != null) {
			this.resourceWSApp2.stop();
			this.resourceWSApp2 = null;
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
		// TransferAgentEditPolicyFactory.unregister();

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
