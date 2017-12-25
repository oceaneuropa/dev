package org.origin.common.rest.server;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.deploy.DeployCallback;
import org.origin.common.rest.Constants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJerseyWSApplication extends ResourceConfig implements IAdaptable, DeployCallback<HttpService> {

	private Logger LOG = LoggerFactory.getLogger(AbstractJerseyWSApplication.class);

	public static final String JSON = MediaType.APPLICATION_JSON;
	public static final String GET = HttpMethod.GET;
	public static final String PUT = HttpMethod.PUT;
	public static final String POST = HttpMethod.POST;
	public static final String DELETE = HttpMethod.DELETE;
	public static final String PATCH = "PATCH";

	protected String contextRoot;
	protected int feature;

	protected ServiceRegistration<?> serviceRegistration;
	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param contextRoot
	 */
	public AbstractJerseyWSApplication(String contextRoot) {
		this(contextRoot, FeatureConstants.PING | FeatureConstants.ECHO);
	}

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public AbstractJerseyWSApplication(String contextRoot, int feature) {
		this.contextRoot = contextRoot;
		this.feature = checkFeature(feature);

		if (hasFeature(FeatureConstants.PING)) {
			// http://{host}:{port}/{contextRoot}/ping
			Resource.Builder pingWSResource = Resource.builder("ping");
			pingWSResource.addMethod(GET).produces(JSON).handledBy(getPingInflector());
			registerResources(pingWSResource.build());
		}

		if (hasFeature(FeatureConstants.ECHO)) {
			// http://{host}:{port}/{contextRoot}/echo?message=<message>
			Resource.Builder echoWSResource = Resource.builder("echo");
			echoWSResource.addMethod(GET).produces(JSON).handledBy(getEchoInflector());
			registerResources(echoWSResource.build());
		}

		if (hasFeature(FeatureConstants.JACKSON)) {
			if (!isEnabled(JacksonFeature.class)) {
				register(JacksonFeature.class);
			}
		}

		if (hasFeature(FeatureConstants.MULTIPLEPART)) {
			if (!isEnabled(MultiPartFeature.class)) {
				register(MultiPartFeature.class);
			}
		}
	}

	protected Inflector<ContainerRequestContext, Response> getPingInflector() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				return Response.ok(1).build();
			}
		};
	}

	protected Inflector<ContainerRequestContext, Response> getEchoInflector() {
		return new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext requestContext) {
				String message = getQueryParam("message", requestContext);
				return Response.ok(message).build();
			}
		};
	}

	public int getFeature() {
		return this.feature;
	}

	protected int checkFeature(int feature) {
		return feature;
	}

	protected boolean hasFeature(int targetFeature) {
		if ((this.feature & targetFeature) == targetFeature) {
			return true;
		}
		return false;
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	public void start(BundleContext bundleContext) {
		LOG.debug("start()");
		if (!this.isStarted.compareAndSet(false, true)) {
			return;
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = bundleContext.registerService(Application.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
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

	public String getPathParam(String paramName, ContainerRequestContext requestContext) {
		return RequestHelper.INSTANCE.getPathParam(paramName, requestContext);
	}

	public List<String> getPathParams(String paramName, ContainerRequestContext requestContext) {
		return RequestHelper.INSTANCE.getPathParams(paramName, requestContext);
	}

	public String getQueryParam(String paramName, ContainerRequestContext requestContext) {
		return RequestHelper.INSTANCE.getQueryParam(paramName, requestContext);
	}

	public List<String> getQueryParams(String paramName, ContainerRequestContext requestContext) {
		return RequestHelper.INSTANCE.getQueryParams(paramName, requestContext);
	}

	public boolean getHasQueryParam(String paramName, ContainerRequestContext requestContext) {
		return RequestHelper.INSTANCE.getHasQueryParam(paramName, requestContext);
	}

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	/** implement DeployCallback<HttpService> interface */
	@Override
	public void deployedTo(HttpService httpService) {
	}

	@Override
	public void undeployedFrom(HttpService httpService) {
	}

}
