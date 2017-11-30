package org.orbit.component.server.tier0.channel.ws;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.orbit.component.server.tier0.channel.service.ChannelService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelWebServiceApplication extends AbstractResourceConfigApplication {

	private static Logger LOG = LoggerFactory.getLogger(ChannelWebServiceApplication.class);

	protected ChannelService service;

	public ChannelWebServiceApplication(BundleContext bundleContext, ChannelService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(ChannelService.class);
			}
		};

		register(serviceBinder);
		register(ChannelWebServiceResource.class);

		// http://{host}:{port}/orbit/v1/channel/ping
		Resource.Builder pingResource = Resource.builder("ping");
		pingResource.addMethod(GET).produces(JSON).handledBy(pingResourceGetHandler());
		registerResources(pingResource.build());
	}

	public ChannelService getChannelService() {
		return this.service;
	}

	protected Inflector<ContainerRequestContext, Response> pingResourceGetHandler() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				int result = (getChannelService() != null) ? 1 : 0;
				return Response.ok(result).build();
			}
		};
	}

	@Override
	public void start() {
		LOG.debug("start() Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is being started.");
		super.start();
	}

	@Override
	public void stop() {
		LOG.debug("start() Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is being stopped.");
		super.stop();
	}

}
