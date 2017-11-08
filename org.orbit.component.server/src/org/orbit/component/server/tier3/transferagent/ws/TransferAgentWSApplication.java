package org.orbit.component.server.tier3.transferagent.ws;

import java.util.Hashtable;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.origin.core.resources.server.service.ResourceService;
import org.origin.core.resources.server.ws.ResourceWSApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * https://www.programcreek.com/java-api-examples/index.php?source_dir=para-master/para-server/src/main/java/com/erudika/para/rest/Api1.java
 *
 */
public class TransferAgentWSApplication extends AbstractResourceConfigApplication {

	protected TransferAgentService service;
	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected TransferAgentServiceTimerV2 serviceIndexTimer;

	protected ResourceWSApplication resourceWSApp1;
	protected ResourceWSApplication resourceWSApp2;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public TransferAgentWSApplication(final BundleContext bundleContext, final TransferAgentService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(TransferAgentService.class);
			}
		});
		register(TransferAgentServiceResource.class);

		Resource.Builder idResource = Resource.builder("id/{id}");
		idResource.addMethod(GET).produces(JSON).handledBy(echoResourceGetHandler());
		registerResources(idResource.build());
	}

	public TransferAgentService getTransferAgentService() {
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

		// Register TransferAgentEditPolicy for TransferAgentServiceResource
		// TransferAgentEditPolicyFactory.register();

		// Register the service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start timer for indexing the service
		if (this.indexProvider != null) {
			this.serviceIndexTimer = new TransferAgentServiceTimerV2(this.indexProvider, this.service);
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

	protected Inflector<ContainerRequestContext, Response> echoResourceGetHandler() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				String message = getPathParam("message", requestContext);
				return Response.ok("echo: '" + message + "' from Server").build();
			}
		};
	}

}

// protected TransferAgentServiceTimer serviceIndexTimer;

// @Override
// public Set<Class<?>> getClasses() {
// Set<Class<?>> classes = new HashSet<Class<?>>();
//
// // resources
// classes.add(TransferAgentServiceResource.class);
//
// // resolvers
// classes.add(TransferAgentResolver.class);
//
// // http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
// // In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
// // Add additional features such as support for Multipart.
// classes.add(MultiPartFeature.class);
//
// return classes;
// }

// private Locale getLocale(String localeStr) {
// try {
// return LocaleUtils.toLocale(localeStr);
// } catch (Exception e) {
// return Locale.US;
// }
// }