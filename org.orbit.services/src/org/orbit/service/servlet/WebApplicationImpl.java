package org.orbit.service.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see OsgiManager
 * @see LogConsole
 *
 */
public abstract class WebApplicationImpl implements WebApplication {

	protected static Logger LOG = LoggerFactory.getLogger(WebApplicationImpl.class);

	protected Map<Object, Object> initProperties;
	protected HashMap<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistration;

	protected List<ServletMetadata> servlets = new ArrayList<ServletMetadata>();
	protected List<ResourceMetadata> resources = new ArrayList<ResourceMetadata>();
	protected HttpContext httpContext;

	protected HttpServletSelfDeployer servletDeployer;

	public WebApplicationImpl() {
		this.initProperties = new HashMap<Object, Object>();
	}

	public WebApplicationImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		if (this.initProperties == null) {
			this.initProperties = new HashMap<Object, Object>();
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		loadProperties(bundleContext);

		addResources();

		// Register WebApplication service.
		// Hashtable<String, Object> props = new Hashtable<String, Object>();
		// this.serviceRegistration = bundleContext.registerService(WebApplication.class, this, props);

		this.servletDeployer = new HttpServletSelfDeployer(new WebApplication[] { this });
		this.servletDeployer.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		// Unregister WebApplication service.
		// if (this.serviceRegistration != null) {
		// this.serviceRegistration.unregister();
		// this.serviceRegistration = null;
		// }

		if (this.servletDeployer != null) {
			this.servletDeployer.stop(bundleContext);
		}
	}

	protected void loadProperties(BundleContext bundleContext) {
		if (this.initProperties != null && !this.initProperties.isEmpty()) {
			this.properties.putAll(this.initProperties);
		}
		String[] propNames = getPropertyNames();
		if (propNames != null) {
			for (String propName : propNames) {
				PropertyUtil.loadProperty(bundleContext, this.properties, propName);
			}
		}
	}

	protected abstract String[] getPropertyNames();

	protected abstract void addResources();

	public Map<Object, Object> getInitProperties() {
		return this.initProperties;
	}

	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	@Override
	public ServletMetadata[] getServlets() {
		return this.servlets.toArray(new ServletMetadata[this.servlets.size()]);
	}

	protected void addServlet(ServletMetadata servletMetadata) {
		if (servletMetadata != null && !this.servlets.contains(servletMetadata)) {
			this.servlets.add(servletMetadata);
		}
	}

	protected void removeServlet(ServletMetadata servletMetadata) {
		if (servletMetadata != null && this.servlets.contains(servletMetadata)) {
			this.servlets.remove(servletMetadata);
		}
	}

	@Override
	public ResourceMetadata[] getResources() {
		return this.resources.toArray(new ResourceMetadata[this.resources.size()]);
	}

	protected void addResource(ResourceMetadata resourceMetadata) {
		if (resourceMetadata != null && !this.resources.contains(resourceMetadata)) {
			this.resources.add(resourceMetadata);
		}
	}

	protected void removeResource(ResourceMetadata resourceMetadata) {
		if (resourceMetadata != null && this.resources.contains(resourceMetadata)) {
			this.resources.remove(resourceMetadata);
		}
	}

	public void setHttpContext(HttpContext httpContext) {
		this.httpContext = httpContext;
	}

	@Override
	public synchronized HttpContext getHttpContext(HttpService httpService) {
		if (this.httpContext == null) {
			this.httpContext = httpService.createDefaultHttpContext();
		}
		return this.httpContext;
	}

}
