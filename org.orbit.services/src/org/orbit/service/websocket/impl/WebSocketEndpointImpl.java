package org.orbit.service.websocket.impl;

import javax.websocket.server.ServerEndpointConfig;

import org.orbit.service.websocket.WebSocketEndpoint;

public class WebSocketEndpointImpl implements WebSocketEndpoint {

	protected ServerEndpointConfig endpointConfig;
	protected Class<?> endpointClass;
	protected boolean isActivated = false;

	public WebSocketEndpointImpl() {
	}

	public WebSocketEndpointImpl(ServerEndpointConfig endpointConfig) {
		this.endpointConfig = endpointConfig;
		this.endpointConfig.getUserProperties().put(WebSocketEndpoint.class.getName(), this);
	}

	public WebSocketEndpointImpl(Class<?> endpointClass) {
		this.endpointClass = endpointClass;
	}

	public WebSocketEndpointImpl(ServerEndpointConfig endpointConfig, Class<?> endpointClass) {
		this.endpointConfig = endpointConfig;
		this.endpointClass = endpointClass;
	}

	public void setEndpointConfig(ServerEndpointConfig endpointConfig) {
		this.endpointConfig = endpointConfig;
	}

	@Override
	public ServerEndpointConfig getEndpointConfig() {
		return this.endpointConfig;
	}

	public void setEndpointClass(Class<?> endpointClass) {
		this.endpointClass = endpointClass;
	}

	@Override
	public Class<?> getEndpointClass() {
		return this.endpointClass;
	}

	@Override
	public boolean isActivated() {
		return this.isActivated;
	}

	@Override
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

}
