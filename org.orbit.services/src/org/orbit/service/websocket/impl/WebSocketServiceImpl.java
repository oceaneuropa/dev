package org.orbit.service.websocket.impl;

import java.util.Hashtable;
import java.util.Map;

import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import org.orbit.service.websocket.WebSocketContext;
import org.orbit.service.websocket.WebSocketService;
import org.osgi.framework.Bundle;

/**
 * @see http://www.eclipse.org/jetty/documentation/9.4.x/embedding-jetty.html
 *
 */
public class WebSocketServiceImpl implements WebSocketService {

	// The bundle associated with this instance of http service
	protected Bundle bundle;
	protected ServerContainer container;
	protected WebSocketContext context;
	protected Map<String, Object> properties;

	/**
	 * 
	 * @param bundle
	 * @param container
	 */
	public WebSocketServiceImpl(Bundle bundle, ServerContainer container) {
		this.bundle = bundle;
		this.container = container;
		this.context = createWebSocketContext(bundle);
	}

	/**
	 * 
	 * @param bundle
	 * @return
	 */
	protected WebSocketContext createWebSocketContext(Bundle bundle) {
		WebSocketContextImpl context = new WebSocketContextImpl();
		context.setBundle(bundle);
		return context;
	}

	public synchronized void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new Hashtable<String, Object>();
		}
		return this.properties;
	}

	@Override
	public WebSocketContext getContext() {
		return this.context;
	}

	@Override
	public void addEndpoint(ServerEndpointConfig endpointConfig) throws Exception {
		if (endpointConfig != null) {
			this.container.addEndpoint(endpointConfig);
		}
	}

	@Override
	public void addEndpoint(Class<?> endpointClass) throws Exception {
		if (endpointClass != null) {
			this.container.addEndpoint(endpointClass);
		}
	}

	public void shutdown() {
	}

}
