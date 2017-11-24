package org.orbit.service.websocket;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class WebSocketServiceListener {

	protected ServiceTracker<WebSocketService, WebSocketService> serviceTracker;
	protected Map<WebSocketService, Map<String, Object>> serviceToPropsMap;
	protected boolean debug = true;

	public WebSocketServiceListener() {
		this.serviceToPropsMap = new LinkedHashMap<WebSocketService, Map<String, Object>>();
	}

	public Set<WebSocketService> getWebSocketServices() {
		return this.serviceToPropsMap.keySet();
	}

	public Map<String, Object> getProperties(WebSocketService service) {
		Map<String, Object> props = this.serviceToPropsMap.get(service);
		if (props == null) {
			props = new Hashtable<String, Object>();
		}
		return props;
	}

	/**
	 * Start tracking org.orbit.service.websocket.WebSocketService services.
	 * 
	 */
	public void start(final BundleContext bundleContext) {
		if (debug) {
			println("WebSocketServiceAdapter is started.");
		}

		this.serviceTracker = new ServiceTracker<WebSocketService, WebSocketService>(bundleContext, WebSocketService.class, new ServiceTrackerCustomizer<WebSocketService, WebSocketService>() {
			@Override
			public WebSocketService addingService(ServiceReference<WebSocketService> reference) {
				WebSocketService service = bundleContext.getService(reference);

				if (debug) {
					println("WebSocketService is added.");
				}

				Hashtable<String, Object> props = new Hashtable<String, Object>();
				for (String propName : reference.getPropertyKeys()) {
					Object propValue = reference.getProperty(propName);
					props.put(propName, propValue);
				}
				serviceToPropsMap.put(service, props);

				added(service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<WebSocketService> reference, WebSocketService service) {
			}

			@Override
			public void removedService(ServiceReference<WebSocketService> reference, WebSocketService service) {
				if (debug) {
					println("WebSocketService is removed.");
				}

				removed(service);

				serviceToPropsMap.remove(service);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking org.orbit.service.websocket.WebSocketService services.
	 * 
	 */
	public void stop(final BundleContext bundleContext) {
		if (debug) {
			println("WebSocketServiceAdapter is stopped.");
		}

		this.serviceTracker.close();

		this.serviceToPropsMap.clear();
	}

	public abstract void added(WebSocketService service);

	public abstract void removed(WebSocketService service);

	protected void println() {
		System.out.println(getClass().getSimpleName());
	}

	protected void println(String msg) {
		System.out.println(getClass().getSimpleName() + " - " + msg);
	}

}
