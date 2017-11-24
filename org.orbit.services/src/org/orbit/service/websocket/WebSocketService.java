package org.orbit.service.websocket;

import java.util.Map;

import javax.websocket.server.ServerEndpointConfig;

public interface WebSocketService {

	Map<String, Object> getProperties();

	WebSocketContext getContext();

	void addEndpoint(ServerEndpointConfig endpointConfig) throws Exception;

	void addEndpoint(Class<?> endpointClass) throws Exception;

}
