package org.example.websocket;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * https://stackoverflow.com/questions/28188172/how-to-access-client-hostname-http-headers-etc-from-a-java-websocket-server
 * 
 */
public class ServletAwareConfigurator extends ServerEndpointConfig.Configurator {

	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		HttpServletRequest httpservletRequest = getField(request, HttpServletRequest.class);
		// String sClientIP = httpservletRequest.getRemoteAddr();
		config.getUserProperties().put("clientip", "remote.address.ip");
		// ...
	}

	// hacking reflector to expose fields...
	private static <I, F> F getField(I instance, Class<F> fieldType) {
		try {
			for (Class<?> type = instance.getClass(); type != Object.class; type = type.getSuperclass()) {
				for (Field field : type.getDeclaredFields()) {
					if (fieldType.isAssignableFrom(field.getType())) {
						field.setAccessible(true);
						return (F) field.get(instance);
					}
				}
			}
		} catch (Exception e) {
			// Handle?
		}
		return null;
	}

}
