package org.orbit.os.server.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.os.server.timer.NodeOSIndexTimerV2;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.ServiceRegistration;

public class NodeOSWSApplication extends AbstractApplication {

	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	// protected NodeOSIndexTimer serviceIndexTimer;
	protected NodeOSIndexTimerV2 serviceIndexTimer;

	public NodeOSWSApplication() {
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

		// Register Application service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(org.origin.common.rest.Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start timer for indexing the service
		this.serviceIndexTimer = new NodeOSIndexTimerV2(this.indexProvider);
		this.serviceIndexTimer.start();
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

		// Unregister Application service
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
		classes.add(NodeOSResource.class);

		// resolvers
		classes.add(NodeOSResolver.class);

		// http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
		// In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
		// Add additional features such as support for Multipart.
		classes.add(MultiPartFeature.class);

		return classes;
	}

}
