package org.orbit.component.server.tier1.auth.ws;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.orbit.component.server.tier1.auth.service.AuthService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.origin.core.resources.server.ws.ResourceWSApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.BundleContext;

/**
 * @see ResourceWSApplication
 *
 */
public class AuthWSApplication extends AbstractResourceConfigApplication {

	protected AuthService service;
	protected IndexProvider indexProvider;
	protected AuthServiceIndexTimer serviceIndexTimer;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public AuthWSApplication(final BundleContext bundleContext, final AuthService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(AuthService.class);
			}
		});
		register(AuthWSResource.class);

		// http://{host}:{port}/{orbit/v1/auth}/ping
		Resource.Builder pingResource = Resource.builder("ping");
		pingResource.addMethod(GET).produces(JSON).handledBy(pingResourceGetHandler());
		registerResources(pingResource.build());

		// http://{host}:{port}/{orbit/v1/auth}/echo/{message}
		Resource.Builder echoResource = Resource.builder("echo/{message}");
		echoResource.addMethod(GET).produces(JSON).handledBy(echoResourceGetHandler());
		registerResources(echoResource.build());
	}

	public AuthService getAuthService() {
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
		if (isStarted()) {
			System.out.println(getClass().getSimpleName() + ".stop() App is already started.");
			return;
		}
		super.start();

		// Start timer for indexing the service
		if (this.indexProvider != null) {
			this.serviceIndexTimer = new AuthServiceIndexTimer(this.indexProvider, this.service);
			this.serviceIndexTimer.start();
		}
	}

	@Override
	public void stop() {
		if (!isStarted()) {
			System.out.println(getClass().getSimpleName() + ".stop() App is already stopped.");
			return;
		}

		// Stop timer for indexing the service
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		super.stop();
	}

	protected Inflector<ContainerRequestContext, Response> pingResourceGetHandler() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				int result = (getAuthService() != null) ? 1 : 0;
				return Response.ok(result).build();
			}
		};
	}

	protected Inflector<ContainerRequestContext, Response> echoResourceGetHandler() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				String message = getPathParam("message", requestContext);
				return Response.ok("Echo '" + message + "' from Server").build();
			}
		};
	}

}
