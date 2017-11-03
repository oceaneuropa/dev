package org.orbit.component.server.tier3.domain.ws;

import java.util.Hashtable;

import javax.ws.rs.core.Application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DomainMgmtWSApplicationV2 extends AbstractResourceConfigApplication {

	protected DomainManagementService service;
	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected DomainMgmtServiceTimerV2 serviceIndexTimer;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public DomainMgmtWSApplicationV2(final BundleContext bundleContext, final DomainManagementService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DomainManagementService.class);
			}
		});
		register(DomainMgmtServiceResource.class);
		register(DomainMgmtMachinesResource.class);
		register(DomainMgmtTransferAgentsResource.class);
		register(DomainMgmtNodesResource.class);
	}

	public DomainManagementService getDomainManagementService() {
		return this.service;
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
			this.serviceIndexTimer = new DomainMgmtServiceTimerV2(this.indexProvider, this.service);
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

}

// @Override
// public Set<Class<?>> getClasses() {
// Set<Class<?>> classes = new HashSet<Class<?>>();
//
// // resources
// classes.add(DomainMgmtServiceResource.class);
// classes.add(DomainMgmtMachinesResource.class);
// classes.add(DomainMgmtTransferAgentsResource.class);
// classes.add(DomainMgmtNodesResource.class);
//
// // resolvers
// classes.add(DomainMgmtServiceResolver.class);
//
// // http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
// // In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
// // Add additional features such as support for Multipart.
// classes.add(MultiPartFeature.class);
//
// return classes;
// }
