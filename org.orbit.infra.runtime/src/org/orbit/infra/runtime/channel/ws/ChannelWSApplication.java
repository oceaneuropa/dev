package org.orbit.infra.runtime.channel.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.channel.service.ChannelService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelWSApplication extends AbstractResourceConfigApplication {

	protected static Logger LOG = LoggerFactory.getLogger(ChannelWSApplication.class);

	protected ChannelService service;

	public ChannelWSApplication(BundleContext bundleContext, ChannelService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(ChannelService.class);
			}
		};
		register(serviceBinder);
		register(ChannelWSResource.class);
	}

	public ChannelService getService() {
		return this.service;
	}

}

// http://{host}:{port}/orbit/v1/channel/ping
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
