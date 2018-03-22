package org.origin.common.rest.server;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.origin.common.rest.util.WebServiceAwareTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class WebServiceAwareRegistryImpl extends WebServiceAwareRegistry {

	protected Map<WebServiceAware, ServiceRegistration<?>> webServiceAwareToServiceRegistrationMap = new HashMap<WebServiceAware, ServiceRegistration<?>>();
	protected WebServiceAwareTracker tracker;

	@Override
	public void start(BundleContext bundleContext) {
		this.tracker = new WebServiceAwareTracker();
		this.tracker.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.tracker != null) {
			this.tracker.stop(bundleContext);
			this.tracker = null;
		}
	}

	@Override
	public WebServiceAware[] getWebServiceAwares() {
		WebServiceAware[] webServiceAwares = null;
		if (this.tracker != null) {
			webServiceAwares = this.tracker.getWebServiceAwares();
		}
		if (webServiceAwares == null) {
			webServiceAwares = WebServiceAware.EMPTY_ARRAY;
		}
		return webServiceAwares;
	}

	@Override
	public void register(BundleContext bundleContext, WebServiceAware webServiceAware) {
		if (webServiceAware == null || this.webServiceAwareToServiceRegistrationMap.containsKey(webServiceAware)) {
			return;
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(WebServiceAware.PROP_NAME, webServiceAware.getName() != null ? webServiceAware.getName() : "n/a");
		props.put(WebServiceAware.PROP_HOST_URL, webServiceAware.getHostURL() != null ? webServiceAware.getHostURL() : "n/a");
		props.put(WebServiceAware.PROP_CONTEXT_ROOT, webServiceAware.getContextRoot() != null ? webServiceAware.getContextRoot() : "n/a");
		ServiceRegistration<?> serviceRegistration = bundleContext.registerService(WebServiceAware.class, webServiceAware, props);
		this.webServiceAwareToServiceRegistrationMap.put(webServiceAware, serviceRegistration);
	}

	@Override
	public void unregister(BundleContext bundleContext, WebServiceAware webServiceAware) {
		if (webServiceAware == null) {
			return;
		}
		ServiceRegistration<?> serviceRegistration = this.webServiceAwareToServiceRegistrationMap.remove(webServiceAware);
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

}
