package org.origin.common.rest.server;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.origin.common.deploy.DeployCallback;
import org.origin.common.rest.Constants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractResourceConfigApplication extends ResourceConfig implements DeployCallback {

	private Logger LOG = LoggerFactory.getLogger(AbstractResourceConfigApplication.class);

	public static final String JSON = MediaType.APPLICATION_JSON;
	public static final String GET = HttpMethod.GET;
	public static final String PUT = HttpMethod.PUT;
	public static final String POST = HttpMethod.POST;
	public static final String DELETE = HttpMethod.DELETE;
	public static final String PATCH = "PATCH";

	protected BundleContext bundleContext;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceRegistration;
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public AbstractResourceConfigApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;

		// if (!isEnabled(JacksonFeature.class)) {
		// register(JacksonFeature.class);
		// }
		// if (!isEnabled(MultiPartFeature.class)) {
		// register(MultiPartFeature.class);
		// }

		// http://{host}:{port}/{contextRoot}/ping
		Resource.Builder pingResource = Resource.builder("ping");
		pingResource.addMethod(GET).produces(JSON).handledBy(pingResourceGetHandler());
		registerResources(pingResource.build());
	}

	protected Inflector<ContainerRequestContext, Response> pingResourceGetHandler() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				return Response.ok(1).build();
			}
		};
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	public void start() {
		LOG.debug("start()");
		if (!this.isStarted.compareAndSet(false, true)) {
			return;
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
	}

	public void stop() {
		LOG.debug("stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public BundleContext getBundleContext() {
		return this.bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void deployedTo(Object target) {
		// System.out.println(getClass().getSimpleName() + ".deployedTo() " + target);

		if (target instanceof HttpService) {
			// HttpService httpService = (HttpService) target;
			// System.out.println(this + " ===> " + httpService);
		}
	}

	@Override
	public void undeployedFrom(Object target) {
		// System.out.println(getClass().getSimpleName() + ".undeployedFrom() " + target);

		if (target instanceof HttpService) {
			// HttpService httpService = (HttpService) target;
			// System.out.println(this + " <=== " + httpService);
		}
	}

	public String getPathParam(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getPathParameters().getFirst(paramName);
	}

	public List<String> getPathParams(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getPathParameters().get(paramName);
	}

	public String getQueryParam(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getQueryParameters().getFirst(paramName);
	}

	public List<String> getQueryParams(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getQueryParameters().get(paramName);
	}

	public boolean getHasQueryParam(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getQueryParameters().containsKey(paramName);
	}

}
