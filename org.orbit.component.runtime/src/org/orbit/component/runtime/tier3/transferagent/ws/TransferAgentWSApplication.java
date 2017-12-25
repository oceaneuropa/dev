package org.orbit.component.runtime.tier3.transferagent.ws;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;

/**
 * https://www.programcreek.com/java-api-examples/index.php?source_dir=para-master/para-server/src/main/java/com/erudika/para/rest/Api1.java
 *
 */
public class TransferAgentWSApplication extends OrbitWSApplication {

	protected TransferAgentService service;

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public TransferAgentWSApplication(final TransferAgentService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(TransferAgentService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(TransferAgentService.class);
			}
		});
		register(TransferAgentWSResource.class);

		// http://{host}:{port}/{contextRoot}/level/{level1}/{level2}?message1=<message1>&message2=<message2>
		Resource.Builder levelWSResource = Resource.builder("level/{level1}/{level2}");
		levelWSResource.addMethod(GET).produces(JSON).handledBy(getLevelInflector());
		registerResources(levelWSResource.build());
	}

	@Override
	protected Inflector<ContainerRequestContext, Response> getEchoInflector() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				TransferAgentService service = getAdapter(TransferAgentService.class);
				String message = getQueryParam("message", requestContext);
				String resultMessage = message + " (from '" + service.getName() + "')";
				return Response.ok(resultMessage).build();
			}
		};
	}

	protected Inflector<ContainerRequestContext, Response> getLevelInflector() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				TransferAgentService service = getAdapter(TransferAgentService.class);
				String level1 = getPathParam("level1", requestContext);
				String level2 = getPathParam("level2", requestContext);
				String message1 = getQueryParam("message1", requestContext);
				String message2 = getQueryParam("message2", requestContext);
				String resultMessage = level1 + "/" + level2 + "?" + message1 + "&" + message2 + " (from '" + service.getName() + "')";
				return Response.ok(resultMessage).build();
			}
		};
	}

}
