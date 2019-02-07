package org.orbit.infra.runtime.datatube.ws;

import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.websocket.server.ServerEndpointConfig;

import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.service.websocket.EndpointService;
import org.orbit.service.websocket.impl.EndpointServiceImpl;
import org.origin.common.service.WebSocketAware;
import org.origin.common.service.WebSocketAwareImpl;
import org.origin.common.service.WebSocketAwareRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Note:
 * 1. This class handles the deployment/undeployment of the ChannelServerEndpoint by registering/unregistering a org.orbit.service.websocket.EndpointAdapter service.
 * 
 * 2. WebSocketDeployer tracks both ServerContainerService services and EndpointAdapter services and deploy WebSocketEndpoint service 
 *    (which provides javax.websocket.server.ServerEndpointConfig) to ServerContainerService service (which provides javax.websocket.server.ServerContainer).
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
public class DataTubeWebSocketApplication {

	protected static Logger LOG = LoggerFactory.getLogger(DataTubeWebSocketApplication.class);

	protected DataTubeService service;
	protected WebSocketAware webSocketAware;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param hostURL
	 * @param webSocketPort
	 * @return
	 */
	public static String getChannelWebSocketURL(String hostURL, String webSocketPort) {
		String channelWebSocketURL = null;
		if (hostURL != null && webSocketPort != null) {
			URI uri = URI.create(hostURL);
			String host = uri.getHost();
			channelWebSocketURL = "ws://" + host + ":" + webSocketPort + "/channel/{channelId}";
		}
		return channelWebSocketURL;
	}

	/**
	 * 
	 * @param service
	 */
	public DataTubeWebSocketApplication(DataTubeService service) {
		this.service = service;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		LOG.info("start()");
		String port = this.service.getWebSocketPort();

		// Register EndpointService for Channels
		ServerEndpointConfig channelEndpointConfig = createEndpointConfigForChannel(this.service);
		EndpointService channelEndpoint = new EndpointServiceImpl(channelEndpointConfig);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("http.port", port);
		this.serviceRegistration = bundleContext.registerService(EndpointService.class, channelEndpoint, props);

		// Register WebSocketAware service
		List<String> urls = new ArrayList<String>();
		String name = this.service.getName();
		String hostURL = this.service.getHostURL();
		String channelWebSocketURL = getChannelWebSocketURL(hostURL, port);
		if (channelWebSocketURL != null) {
			urls.add(channelWebSocketURL);
		}
		this.webSocketAware = new WebSocketAwareImpl(name, urls);
		WebSocketAwareRegistry.getInstance().register(bundleContext, this.webSocketAware);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		LOG.info("stop()");

		// Unregister WebSocketAware service
		if (this.webSocketAware != null) {
			WebSocketAwareRegistry.getInstance().unregister(bundleContext, this.webSocketAware);
		}

		// Unregister EndpointService
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
	protected ServerEndpointConfig createEndpointConfigForChannel(DataTubeService service) {
		ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(ChannelServerEndpoint.class, "/channel/{channelId}");
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
