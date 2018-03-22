package org.orbit.component.runtime.tier1.auth.ws;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.Inflector;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.origin.common.rest.util.WebServiceAware;

public class AuthWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public AuthWSApplication(final AuthService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(AuthService.class, service);
		adapt(WebServiceAware.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(AuthService.class);
			}
		});
		register(AuthWSResource.class);
	}

	@Override
	protected Inflector<ContainerRequestContext, Response> getEchoInflector() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				AuthService service = getAdapter(AuthService.class);
				String message = getQueryParam("message", requestContext);
				String resultMessage = message + " (from '" + service.getName() + "')";
				return Response.ok(resultMessage).build();
			}
		};
	}

}
