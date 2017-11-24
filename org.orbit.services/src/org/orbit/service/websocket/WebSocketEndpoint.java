package org.orbit.service.websocket;

import javax.websocket.server.ServerEndpointConfig;

/**
 * Remove endpoint from websocket ServerContainer
 * 
 * @see https://stackoverflow.com/questions/33611866/remove-endpoint-from-servercontainer-inside-weblistener
 *
 */
public interface WebSocketEndpoint {

	ServerEndpointConfig getEndpointConfig();

	Class<?> getEndpointClass();

	boolean isActivated();

	void setActivated(boolean isActivated);

}
