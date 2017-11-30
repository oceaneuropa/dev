package org.orbit.component.server.tier0.channel.ws;

import java.util.Hashtable;

import javax.websocket.server.ServerEndpointConfig;

import org.orbit.component.server.tier0.channel.service.ChannelService;
import org.orbit.service.websocket.WebSocketEndpoint;
import org.orbit.service.websocket.impl.WebSocketEndpointImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://technology.amis.nl/2015/05/14/java-web-application-sending-json-messages-through-websocket-to-html5-browser-application-for-real-time-push/
 * 
 * @see https://stackoverflow.com/questions/21763829/jsr-356-how-to-abort-a-websocket-connection-during-the-handshake
 * 
 *      JSR-356: How to abort a websocket connection during the handshake?
 * 
 */
public class ChannelWebSocket {

	protected static Logger LOG = LoggerFactory.getLogger(ChannelWebSocket.class);

	protected ChannelService service;
	protected ServiceRegistration<?> channelEndpointServiceRegistration;

	public ChannelWebSocket(ChannelService service) {
		this.service = service;
	}

	public void start(final BundleContext bundleContext) {
		LOG.info("start()");

		ServerEndpointConfig channelEndpointConfig = createChannelEndpointConfig(this.service);
		WebSocketEndpoint channelEndpointService = new WebSocketEndpointImpl(channelEndpointConfig);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("http.port", this.service.getHttpPort());
		this.channelEndpointServiceRegistration = bundleContext.registerService(WebSocketEndpoint.class, channelEndpointService, props);
	}

	public void stop(final BundleContext bundleContext) {
		LOG.info("stop()");

		if (this.channelEndpointServiceRegistration != null) {
			this.channelEndpointServiceRegistration.unregister();
			this.channelEndpointServiceRegistration = null;
		}
	}

	protected ServerEndpointConfig createChannelEndpointConfig(ChannelService service) {
		ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(ChannelWebSocketEndpoint.class, ChannelWebSocketEndpoint.getPath());
		ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator();
		configBuilder.configurator(configConfigurator);
		ServerEndpointConfig endpointConfig = configBuilder.build();
		endpointConfig.getUserProperties().put(ChannelService.class.getName(), service);
		return endpointConfig;
	}

	// protected ServerEndpointConfig.Configurator getConfigurator1() {
	// return new ServerEndpointConfig.Configurator();
	// }

	// protected ServerEndpointConfig.Configurator getConfigurator2() {
	// ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator() {
	// @Override
	// public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
	// LOG.info("ServerEndpointConfig.Configurator.modifyHandshake()");
	// // super.modifyHandshake(config, request, response);
	//
	// // URI requestURI = request.getRequestURI(); // ws://localhost:7001/channel/room1
	// // String queryString = request.getQueryString(); // null
	// // Object httpSession = request.getHttpSession(); // null
	// // Principal principal = request.getUserPrincipal(); // null
	// // Map<String, List<String>> params = request.getParameterMap(); // empty
	//
	// // Example:
	// // ------------------------------------------------------------
	// // {
	// // Authorization=[Basic dXNlcm5hbWU6cGFzc3dvcmQ=]
	// // Cache-Control=[no-cache],
	// // Connection=[Upgrade],
	// // Host=[localhost:7001],
	// // Pragma=[no-cache],
	// // Sec-WebSocket-Key=[U1zPynay/5d7sruSb5pfIQ==],
	// // Sec-WebSocket-Version=[13],
	// // Upgrade=[websocket]
	// // }
	// // ------------------------------------------------------------
	// Map<String, List<String>> headers = request.getHeaders();
	//
	// boolean isAuthorized = false;
	// Object authorization = headers.get("Authorization");
	// if (authorization != null) {
	// // get JWT from it and verify the username
	// }
	// if (!isAuthorized) {
	// // headers.put(HandshakeResponse.SEC_WEBSOCKET_ACCEPT, new ArrayList<String>());
	// // headers.remove(HandshakeResponse.SEC_WEBSOCKET_ACCEPT);
	// // return;
	// }
	//
	// for (Iterator<String> headerNameItor = headers.keySet().iterator(); headerNameItor.hasNext();) {
	// String headerName = headerNameItor.next();
	// boolean ignore = false;
	// if ("Cache-Control".equals(headerName) //
	// || "Connection".equals(headerName) //
	// || "Host".equals(headerName) //
	// || "Pragma".equals(headerName) //
	// || "Sec-WebSocket-Key".equals(headerName) //
	// || "Sec-WebSocket-Version".equals(headerName) //
	// || "Upgrade".equals(headerName) //
	// ) {
	// ignore = true;
	// }
	// if (ignore) {
	// continue;
	// }
	// List<String> headerValues = headers.get(headerName);
	// if (headerValues != null) {
	// config.getUserProperties().put(headerName, headerValues);
	// }
	// }
	// }
	// };
	// return configConfigurator;
	// }

}
