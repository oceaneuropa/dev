package org.orbit.infra.runtime.datatube.ws;

import java.util.Hashtable;

import javax.websocket.server.ServerEndpointConfig;

import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.service.websocket.WebSocketEndpoint;
import org.orbit.service.websocket.impl.WebSocketEndpointImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Note:
 * 1. This class handles the deployment/undeployment of the ChannelWebSocketServerEndpoint by registering/unregistering WebSocketEndpoint service.
 * 
 * 2. WebSocketDeployer tracks both ServerContainerService services and WebSocketEndpoint services and deploy WebSocketEndpoint service 
 *    (providing javax.websocket.server.ServerEndpointConfig) to ServerContainerService service (providing javax.websocket.server.ServerContainer).
 * 
 * 3. ServerContainerServiceImpl deploys javax.websocket.server.ServerEndpointConfig (from ServerContainerService) to javax.websocket.server.ServerContainer (from ServerContainerServiceImpl).
 *    (javax.websocket.server.ServerContainer doesn't have API for undeploying javax.websocket.server.ServerEndpointConfig.)
 *    
 * 4. ServerContainerServiceImpl is created by ServerContainerServiceFactory (OSGi service factory for ServerContainerService service).
 * 
 * 5. ServerContainerAdapter tracks javax.websocket.server.ServerContainer service. For a added javax.websocket.server.ServerContainer service, ServerContainerAdapter creates 
 *    ServerContainerServiceFactory (ServiceFactory<ServerContainerService>) with the ServerContainer and register the ServerContainerServiceFactory as ServerContainerService service.
 *
 * 6. ServerContainerAdapter is started when the org.orbit.service bundle's activator is started.
 * 
 * References:
 * 1. @see https://technology.amis.nl/2015/05/14/java-web-application-sending-json-messages-through-websocket-to-html5-browser-application-for-real-time-push/
 * 2. JSR-356: How to abort a websocket connection during the handshake? 
 *    https://stackoverflow.com/questions/21763829/jsr-356-how-to-abort-a-websocket-connection-during-the-handshake
 * 3. Example:
 *    https://www.programcreek.com/java-api-examples/index.php?source_dir=jetty-web-sockets-jsr356-master/src/main/java/com/example/ws/ServerStarter.java
 * 
 */
public class DataTubeWebSocketHandler {

	protected static Logger LOG = LoggerFactory.getLogger(DataTubeWebSocketHandler.class);

	protected DataTubeService service;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param service
	 */
	public DataTubeWebSocketHandler(DataTubeService service) {
		this.service = service;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		LOG.info("start()");

		ServerEndpointConfig channelEndpointConfig = createChannelEndpointConfig(this.service);
		WebSocketEndpoint channelEndpoint = new WebSocketEndpointImpl(channelEndpointConfig);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("http.port", this.service.getWebSocketHttpPort());
		this.serviceRegistration = bundleContext.registerService(WebSocketEndpoint.class, channelEndpoint, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		LOG.info("stop()");

		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	/**
	 * 
	 * @param service
	 * @return
	 */
	protected ServerEndpointConfig createChannelEndpointConfig(DataTubeService service) {
		ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(DataTubeWebSocketServerEndpoint.class, "/channel/{channelId}");
		ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator();
		configBuilder.configurator(configConfigurator);
		ServerEndpointConfig endpointConfig = configBuilder.build();
		endpointConfig.getUserProperties().put(DataTubeService.class.getName(), service);
		return endpointConfig;
	}

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
