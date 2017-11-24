package org.orbit.service.websocket;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.websocket.server.ServerContainer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * 1. When a javax.websocket.server.ServerContainer service is registered, create a WebSocketServiceFactory with it and register the WebSocketServiceFactory as
 * a factory for WebSocketService. WebSocketServiceFactory creates one instance of WebSocketService for each client bundle using the WebSocketService.
 * 
 * 2. When a javax.websocket.server.ServerContainer service is unregistered, unregister the WebSocketServiceFactory.
 * 
 * 3. When the adapter is started, it starts tracking javax.websocket.server.ServerContainer services.
 * 
 * 4. When the adapter is stopped, it stops tracking javax.websocket.server.ServerContainer services. For already tracked ServerContainer services, unregister
 * the corresponding WebSocketServiceFactory.
 * 
 */
public class ServerContainerAdapter {

	protected ServiceTracker<ServerContainer, ServerContainer> serviceTracker;
	protected Map<ServerContainer, ServiceRegistration<WebSocketService>> containerToWebSocketServiceFactoryReferenceMap;
	protected boolean debug = true;

	public ServerContainerAdapter() {
		this.containerToWebSocketServiceFactoryReferenceMap = new LinkedHashMap<ServerContainer, ServiceRegistration<WebSocketService>>();
	}

	/**
	 * Start tracking javax.websocket.server.ServerContainer services.
	 * 
	 */
	public void start(final BundleContext bundleContext) {
		if (debug) {
			println("ServerContainerAdapter is started.");
		}

		this.serviceTracker = new ServiceTracker<ServerContainer, ServerContainer>(bundleContext, ServerContainer.class, new ServiceTrackerCustomizer<ServerContainer, ServerContainer>() {
			@Override
			public ServerContainer addingService(ServiceReference<ServerContainer> reference) {
				ServerContainer container = bundleContext.getService(reference);

				if (debug) {
					println("ServerContainer service is added.");
				}

				registerWebSocketServiceFactory(bundleContext, reference, container);

				return container;
			}

			@Override
			public void modifiedService(ServiceReference<ServerContainer> reference, ServerContainer container) {
			}

			@Override
			public void removedService(ServiceReference<ServerContainer> reference, ServerContainer container) {
				if (debug) {
					println("ServerContainer service is removed.");
				}

				unregisterWebSocketServiceFactory(bundleContext, reference, container);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking javax.websocket.server.ServerContainer services.
	 * 
	 * For already tracked ServerContainer services, unregister the corresponding WebSocketServiceFactory.
	 * 
	 */
	public void stop(final BundleContext bundleContext) {
		if (debug) {
			println("ServerContainerAdapter is stopped.");
		}

		for (Iterator<ServerContainer> itor = this.containerToWebSocketServiceFactoryReferenceMap.keySet().iterator(); itor.hasNext();) {
			ServerContainer container = itor.next();

			if (debug) {
				println("Unregister WebSocketService for ServerContainer " + container);
			}

			ServiceRegistration<WebSocketService> serviceRegistration = this.containerToWebSocketServiceFactoryReferenceMap.get(container);
			if (serviceRegistration != null) {
				serviceRegistration.unregister();
			}
		}
		this.containerToWebSocketServiceFactoryReferenceMap.clear();

		this.serviceTracker.close();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param reference
	 * @param container
	 */
	public void registerWebSocketServiceFactory(BundleContext bundleContext, ServiceReference<ServerContainer> reference, ServerContainer container) {
		if (debug) {
			println("Register WebSocketServiceFactory for ServerContainer " + container);
		}

		String[] propNames = reference.getPropertyKeys();
		for (String propName : propNames) {
			Object propValue = reference.getProperty(propName);
			println("\t" + propName + " = " + propValue);
		}

		WebSocketServiceFactory factory = new WebSocketServiceFactory(container);
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		for (String propName : propNames) {
			Object propValue = reference.getProperty(propName);
			props.put(propName, propValue);
		}
		factory.setProperties(props);

		ServiceRegistration<WebSocketService> serviceRegistration = bundleContext.registerService(WebSocketService.class, factory, props);
		if (serviceRegistration != null) {
			this.containerToWebSocketServiceFactoryReferenceMap.put(container, serviceRegistration);
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param reference
	 * @param container
	 */
	public void unregisterWebSocketServiceFactory(BundleContext bundleContext, ServiceReference<ServerContainer> reference, ServerContainer container) {
		if (debug) {
			println("Unregister WebSocketServiceFactory for ServerContainer " + container);
		}

		ServiceRegistration<WebSocketService> serviceRegistration = this.containerToWebSocketServiceFactoryReferenceMap.remove(container);
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

	protected void println() {
		System.out.println(getClass().getSimpleName());
	}

	protected void println(String msg) {
		System.out.println(getClass().getSimpleName() + " - " + msg);
	}

}
