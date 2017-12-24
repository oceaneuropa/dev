package org.orbit.component.runtime.tier3.domain.ws.other;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceWSMachinesResource;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceWSNodesResource;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceWSTransferAgentsResource;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.osgi.framework.ServiceRegistration;

public class DomainMgmtWSApplicationV1 extends AbstractApplication {

	// protected static Logger logger = LoggerFactory.getLogger(DomainMgmtWSApplication.class);

	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected DomainMgmtServiceTimerV1 serviceIndexTimer;

	public DomainMgmtWSApplicationV1() {
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

		// Register the service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start timer for indexing the service
		if (this.indexProvider != null) {
			this.serviceIndexTimer = new DomainMgmtServiceTimerV1(this.indexProvider);
			// The web application knows its DomainMgmtServiceResource provides a ping method.
			// So it tells the index timer that the web service can is pingable.
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

		super.stop();
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(DomainMgmtWSServiceResourceV1.class);
		classes.add(DomainServiceWSMachinesResource.class);
		classes.add(DomainServiceWSTransferAgentsResource.class);
		classes.add(DomainServiceWSNodesResource.class);

		// resolvers
		classes.add(DomainMgmtServiceResolver.class);

		// http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
		// In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
		// Add additional features such as support for Multipart.
		classes.add(MultiPartFeature.class);

		return classes;
	}

}
