package org.orbit.os.server.ws;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.orbit.os.server.service.NodeOS;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeOSWebServiceApplication extends AbstractResourceConfigApplication {

	protected static Logger LOG = LoggerFactory.getLogger(NodeOSWebServiceApplication.class);

	protected ServiceRegistration<?> serviceRegistration;
	protected NodeOS service;

	public NodeOSWebServiceApplication(BundleContext bundleContext, NodeOS service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(NodeOS.class);
			}
		};

		register(serviceBinder);
		register(NodeOSWebServiceResource.class);

		if (!isEnabled(JacksonFeature.class)) {
			register(JacksonFeature.class);
		}
		if (!isEnabled(MultiPartFeature.class)) {
			register(MultiPartFeature.class);
		}

		Resource.Builder pingResource = Resource.builder("ping");
		pingResource.addMethod(GET).produces(JSON).handledBy(pingResourceGetHandler());
		registerResources(pingResource.build());
	}

	public NodeOS getService() {
		return this.service;
	}

	protected Inflector<ContainerRequestContext, Response> pingResourceGetHandler() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				int result = (getService() != null) ? 1 : 0;
				return Response.ok(result).build();
			}
		};
	}

}
