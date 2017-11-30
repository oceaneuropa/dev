package jetty.example2.server;

import java.util.Hashtable;

import javax.websocket.server.ServerEndpointConfig;

import org.orbit.service.websocket.WebSocketEndpoint;
import org.orbit.service.websocket.impl.WebSocketEndpointImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class WebSocketServerOSGi {

	// protected ServiceRegistration<?> serviceRegistration1;
	protected ServiceRegistration<?> serviceRegistration2;
	protected ServiceRegistration<?> serviceRegistration3;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		// this.endpoint1 = createWebSocketEndpoint1();
		// Hashtable<String, Object> props1 = new Hashtable<String, Object>();
		// props1.put("http.port", "7001");
		// this.serviceRegistration1 = bundleContext.registerService(WebSocketEndpoint.class, this.endpoint1, props1);

		ServerEndpointConfig endpointConfig2 = createEndpointConfig2();
		WebSocketEndpoint endpoint2 = new WebSocketEndpointImpl(endpointConfig2);
		Hashtable<String, Object> props2 = new Hashtable<String, Object>();
		props2.put("http.port", "7001");
		this.serviceRegistration2 = bundleContext.registerService(WebSocketEndpoint.class, endpoint2, props2);

		ServerEndpointConfig endpointConfig3 = createEndpointConfig3();
		WebSocketEndpoint endpoint3 = new WebSocketEndpointImpl(endpointConfig3);
		Hashtable<String, Object> props3 = new Hashtable<String, Object>();
		props3.put("http.port", "7001");
		this.serviceRegistration3 = bundleContext.registerService(WebSocketEndpoint.class, endpoint3, props3);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// if (this.serviceRegistration1 != null) {
		// this.serviceRegistration1.unregister();
		// this.serviceRegistration1 = null;
		// }
		if (this.serviceRegistration2 != null) {
			this.serviceRegistration2.unregister();
			this.serviceRegistration2 = null;
		}
		if (this.serviceRegistration3 != null) {
			this.serviceRegistration3.unregister();
			this.serviceRegistration3 = null;
		}
	}

	protected ServerEndpointConfig createEndpointConfig2() {
		ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(ToUpper356Socket.class, "/orbit/jsr356toUpper");

		// List<String> subprotocols = Arrays.asList(new String[] { "http", "rest", "json", "xml" });
		// List<String> subprotocols = new ArrayList<>();
		// List<Extension> extensions = new ArrayList<>();
		// List<Class<? extends Encoder>> encoders = new ArrayList<>();
		// List<Class<? extends Decoder>> decoders = new ArrayList<>();
		// configBuilder.subprotocols(subprotocols);
		// configBuilder.extensions(extensions);
		// configBuilder.encoders(encoders);
		// configBuilder.decoders(decoders);

		ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator();
		configBuilder.configurator(configConfigurator);

		ServerEndpointConfig endpointConfig = configBuilder.build();
		endpointConfig.getUserProperties().put("webSocketTransport", "webSocketTransport1");
		endpointConfig.getUserProperties().put("contextPath", "contextPath1");
		endpointConfig.getUserProperties().put("endpointPath", "endpointPath1");
		endpointConfig.getUserProperties().put("context_root", "/orbit/jsr356toUpper2");

		return endpointConfig;
	}

	protected ServerEndpointConfig createEndpointConfig3() {
		ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(MyEndpoint1.class, "/websocket/{myPathParam}");

		ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator();
		configBuilder.configurator(configConfigurator);

		ServerEndpointConfig endpointConfig = configBuilder.build();
		endpointConfig.getUserProperties().put("p1", "v1");
		endpointConfig.getUserProperties().put("p2", "v2");
		endpointConfig.getUserProperties().put("context_root", "/websocket/{myPathParam}");

		return endpointConfig;
	}

}

// protected WebSocketEndpoint createWebSocketEndpoint1() {
// WebSocketEndpoint endpoint = new WebSocketEndpoint() {
// @Override
// public ServerEndpointConfig getServerEndpointConfig() {
// ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(WebSocketServerEndpoint.class, "/orbit/skywalker");
//
// // List<String> subprotocols = Arrays.asList(new String[] { "http", "rest", "json", "xml" });
// List<String> subprotocols = new ArrayList<>();
// List<Extension> extensions = new ArrayList<>();
// List<Class<? extends Encoder>> encoders = new ArrayList<>();
// List<Class<? extends Decoder>> decoders = new ArrayList<>();
// configBuilder.subprotocols(subprotocols);
// configBuilder.extensions(extensions);
// configBuilder.encoders(encoders);
// configBuilder.decoders(decoders);
//
// ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator() {
// };
// configBuilder.configurator(configConfigurator);
//
// ServerEndpointConfig serverEndpointConfig = configBuilder.build();
// serverEndpointConfig.getUserProperties().put("webSocketTransport", "webSocketTransport1");
// serverEndpointConfig.getUserProperties().put("contextPath", "contextPath1");
// serverEndpointConfig.getUserProperties().put("endpointPath", "endpointPath1");
//
// return serverEndpointConfig;
// }
//
// @Override
// public Class<?> getServerEndpointClass() {
// return null;
// }
// };
// return endpoint;
// }
