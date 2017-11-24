package org.orbit.service.websocket;

import java.util.Hashtable;
import java.util.Map;

import javax.websocket.server.ServerContainer;

import org.orbit.service.websocket.impl.WebSocketServiceImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

public class WebSocketServiceFactory implements ServiceFactory<WebSocketService> {

	protected ServerContainer serverContainer;
	protected Map<String, Object> properties;

	/**
	 * 
	 * @param serverContainer
	 */
	public WebSocketServiceFactory(ServerContainer serverContainer) {
		this.serverContainer = serverContainer;
	}

	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new Hashtable<String, Object>();
		}
		return this.properties;
	}

	public synchronized void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public WebSocketService getService(Bundle bundle, ServiceRegistration<WebSocketService> registration) {
		WebSocketServiceImpl service = new WebSocketServiceImpl(bundle, this.serverContainer);
		service.setProperties(getProperties());
		return service;
	}

	@Override
	public void ungetService(Bundle bundle, ServiceRegistration<WebSocketService> registration, WebSocketService service) {
		if (service instanceof WebSocketServiceImpl) {
			((WebSocketServiceImpl) service).shutdown();
		}
	}

}
