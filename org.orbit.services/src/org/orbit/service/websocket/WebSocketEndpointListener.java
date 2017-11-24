package org.orbit.service.websocket;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class WebSocketEndpointListener {

	protected ServiceTracker<WebSocketEndpoint, WebSocketEndpoint> serviceTracker;
	protected Map<WebSocketEndpoint, Map<String, Object>> serviceToPropsMap;
	protected boolean debug = true;

	public WebSocketEndpointListener() {
		this.serviceToPropsMap = new HashMap<WebSocketEndpoint, Map<String, Object>>();
	}

	public Set<WebSocketEndpoint> getEndpoints() {
		return this.serviceToPropsMap.keySet();
	}

	public Map<String, Object> getProperties(WebSocketEndpoint endpoint) {
		Map<String, Object> props = this.serviceToPropsMap.get(endpoint);
		if (props == null) {
			props = new Hashtable<String, Object>();
		}
		return props;
	}

	/**
	 * Start tracking org.orbit.service.websocket.WebSocketEndpoint services.
	 * 
	 */
	public void start(final BundleContext bundleContext) {
		if (debug) {
			println("WebSocketEndpointAdapter is started.");
		}

		this.serviceTracker = new ServiceTracker<WebSocketEndpoint, WebSocketEndpoint>(bundleContext, WebSocketEndpoint.class, new ServiceTrackerCustomizer<WebSocketEndpoint, WebSocketEndpoint>() {
			@Override
			public WebSocketEndpoint addingService(ServiceReference<WebSocketEndpoint> reference) {
				WebSocketEndpoint endpoint = bundleContext.getService(reference);

				if (debug) {
					println("WebSocketEndpoint is added.");
				}

				Hashtable<String, Object> props = new Hashtable<String, Object>();
				for (String propName : reference.getPropertyKeys()) {
					Object propValue = reference.getProperty(propName);
					props.put(propName, propValue);
				}
				serviceToPropsMap.put(endpoint, props);

				added(endpoint);

				return endpoint;
			}

			@Override
			public void modifiedService(ServiceReference<WebSocketEndpoint> reference, WebSocketEndpoint endpoint) {
			}

			@Override
			public void removedService(ServiceReference<WebSocketEndpoint> reference, WebSocketEndpoint endpoint) {
				if (debug) {
					println("WebSocketEndpoint is removed.");
				}

				removed(endpoint);

				serviceToPropsMap.remove(endpoint);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking org.orbit.service.websocket.WebSocketEndpoint services.
	 * 
	 */
	public void stop(final BundleContext bundleContext) {
		if (debug) {
			println("WebSocketEndpointAdapter is stopped.");
		}

		this.serviceTracker.close();

		this.serviceToPropsMap.clear();
	}

	public abstract void added(WebSocketEndpoint endpoint);

	public abstract void removed(WebSocketEndpoint endpoint);

	protected void println() {
		System.out.println(getClass().getSimpleName());
	}

	protected void println(String msg) {
		System.out.println(getClass().getSimpleName() + " - " + msg);
	}

}
