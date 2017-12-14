package org.orbit.os.runtime.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.os.runtime.service.GAIA;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GaiaWSApplication extends AbstractResourceConfigApplication {

	protected static Logger LOG = LoggerFactory.getLogger(GaiaWSApplication.class);

	protected ServiceRegistration<?> serviceRegistration;
	protected GAIA gaia;

	public GaiaWSApplication(final BundleContext bundleContext, final GAIA gaia) {
		super(bundleContext, gaia.getContextRoot());
		this.gaia = gaia;

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(gaia).to(GAIA.class);
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
		return this.gaia;
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