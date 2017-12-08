package org.orbit.os.server.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.os.server.service.GAIA;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GaiaWSApplication extends AbstractResourceConfigApplication {

	protected static Logger LOG = LoggerFactory.getLogger(GaiaWSApplication.class);

	protected ServiceRegistration<?> serviceRegistration;
	protected GAIA service;

	public GaiaWSApplication(BundleContext bundleContext, GAIA service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(GAIA.class);
			}
		};

		register(serviceBinder);
		register(GaiaWSResource.class);

		if (!isEnabled(JacksonFeature.class)) {
			register(JacksonFeature.class);
		}
		if (!isEnabled(MultiPartFeature.class)) {
			register(MultiPartFeature.class);
		}
	}

	public GAIA getService() {
		return this.service;
	}

}

// Resource.Builder pingResource = Resource.builder("ping");
// pingResource.addMethod(GET).produces(JSON).handledBy(pingResourceGetHandler());
// registerResources(pingResource.build());

// protected Inflector<ContainerRequestContext, Response> pingResourceGetHandler() {
// return new Inflector<ContainerRequestContext, Response>() {
// @Override
// public Response apply(ContainerRequestContext requestContext) {
// int result = (getService() != null) ? 1 : 0;
// return Response.ok(result).build();
// }
// };
// }