package org.orbit.component.server.tier3.transferagent.ws;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.origin.core.resources.server.service.ResourceService;
import org.origin.core.resources.server.ws.ResourcesWebServiceApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://www.programcreek.com/java-api-examples/index.php?source_dir=para-master/para-server/src/main/java/com/erudika/para/rest/Api1.java
 *
 */
public class TransferAgentWebServiceApplication extends AbstractResourceConfigApplication {

	protected static Logger LOG = LoggerFactory.getLogger(TransferAgentWebServiceApplication.class);

	protected TransferAgentService service;
	protected ResourcesWebServiceApplication resourceWSApp1;
	protected ResourcesWebServiceApplication resourceWSApp2;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public TransferAgentWebServiceApplication(final BundleContext bundleContext, final TransferAgentService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(TransferAgentService.class);
			}
		};
		register(serviceBinder);
		register(TransferAgentServiceResource.class);

		Resource.Builder idResource = Resource.builder("id/{id}");
		idResource.addMethod(GET).produces(JSON).handledBy(echoResourceGetHandler());
		registerResources(idResource.build());
	}

	public TransferAgentService getService() {
		return this.service;
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

	@Override
	public void start() {
		LOG.info("start()");

		super.start();
		this.isStarted.set(true);

		String contextRoot1 = this.contextRoot + "/root1"; // e.g. /orbit/v1/ta/root1
		ResourceService resourceService1 = new ResourceService();
		resourceService1.setName("root1");
		resourceService1.setContextRoot(contextRoot1);
		this.resourceWSApp1 = new ResourcesWebServiceApplication(this.bundleContext, resourceService1);

		String contextRoot2 = this.contextRoot + "/root2"; // e.g. /orbit/v1/ta/root2
		ResourceService resourceService2 = new ResourceService();
		resourceService2.setName("root2");
		resourceService2.setContextRoot(contextRoot2);
		this.resourceWSApp2 = new ResourcesWebServiceApplication(this.bundleContext, resourceService2);

		this.resourceWSApp1.start();
		this.resourceWSApp2.start();
	}

	@Override
	public void stop() {
		LOG.info("stop()");

		if (this.resourceWSApp1 != null) {
			this.resourceWSApp1.stop();
			this.resourceWSApp1 = null;
		}
		if (this.resourceWSApp2 != null) {
			this.resourceWSApp2.stop();
			this.resourceWSApp2 = null;
		}

		super.stop();
	}

}

// Register TransferAgentEditPolicy for TransferAgentServiceResource
// TransferAgentEditPolicyFactory.register();

// Unregister TransferAgentEditPolicy for TransferAgentServiceResource
// TransferAgentEditPolicyFactory.unregister();

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
