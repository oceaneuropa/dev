package jetty.example2.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.websocket.Decoder;
import javax.websocket.DeploymentException;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

/*
 * Example
 * https://www.programcreek.com/java-api-examples/index.php?source_dir=jetty-web-sockets-jsr356-master/src/main/java/com/example/ws/ServerStarter.java
 * 
 */
public class WebSocketServerMain {

	protected int port;
	protected Server server;

	public WebSocketServerMain(int port) {
		this.port = port;
	}

	public void start() {
		this.server = new Server();

		ServerConnector connector = new ServerConnector(this.server);
		connector.setPort(this.port);
		this.server.addConnector(connector);

		ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContext.setContextPath("/*");
		this.server.setHandler(servletContext);
		try {
			ServerContainer serverContainer = WebSocketServerContainerInitializer.configureContext(servletContext);
			addWebSocketServerEndpoint(serverContainer);
			// addToUpper356Socket_v1(serverContainer);
			addToUpper356Socket_v2(serverContainer);

		} catch (ServletException e) {
			e.printStackTrace();
		}

		try {
			this.server.start();
			System.out.println("--- start 1 ---");
			this.server.join();
			System.out.println("--- start 2 ---");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			System.out.println("--- stop 1 ---");
			this.server.stop();
			System.out.println("--- stop 2 ---");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void addWebSocketServerEndpoint(ServerContainer serverContainer) {
		ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(WebSocketServerEndpoint.class, "/orbit/skywalker");

		// List<String> subprotocols = Arrays.asList(new String[] { "http", "rest", "json", "xml" });
		List<String> subprotocols = new ArrayList<>();
		List<Extension> extensions = new ArrayList<>();
		List<Class<? extends Encoder>> encoders = new ArrayList<>();
		List<Class<? extends Decoder>> decoders = new ArrayList<>();
		configBuilder.subprotocols(subprotocols);
		configBuilder.extensions(extensions);
		configBuilder.encoders(encoders);
		configBuilder.decoders(decoders);

		// ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator() {
		// };
		ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator();
		configBuilder.configurator(configConfigurator);

		ServerEndpointConfig serverEndpointConfig = configBuilder.build();
		serverEndpointConfig.getUserProperties().put("webSocketTransport", "webSocketTransport1");
		serverEndpointConfig.getUserProperties().put("contextPath", "contextPath1");
		serverEndpointConfig.getUserProperties().put("endpointPath", "endpointPath1");

		try {
			// install websocket endpoint
			serverContainer.addEndpoint(serverEndpointConfig);
		} catch (DeploymentException e) {
			e.printStackTrace();
		}
	}

	protected void addToUpper356Socket_v1(ServerContainer serverContainer) {
		try {
			serverContainer.addEndpoint(ToUpper356Socket.class);
		} catch (DeploymentException e) {
			e.printStackTrace();
		}
	}

	protected void addToUpper356Socket_v2(ServerContainer serverContainer) {
		ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(ToUpper356Socket.class, "/orbit/jsr356toUpper");

		// List<String> subprotocols = Arrays.asList(new String[] { "http", "rest", "json", "xml" });
		List<String> subprotocols = new ArrayList<>();
		List<Extension> extensions = new ArrayList<>();
		List<Class<? extends Encoder>> encoders = new ArrayList<>();
		List<Class<? extends Decoder>> decoders = new ArrayList<>();
		configBuilder.subprotocols(subprotocols);
		configBuilder.extensions(extensions);
		configBuilder.encoders(encoders);
		configBuilder.decoders(decoders);

		// ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator() {
		// };
		ServerEndpointConfig.Configurator configConfigurator = new ServerEndpointConfig.Configurator();
		configBuilder.configurator(configConfigurator);

		ServerEndpointConfig serverEndpointConfig = configBuilder.build();
		serverEndpointConfig.getUserProperties().put("webSocketTransport", "webSocketTransport1");
		serverEndpointConfig.getUserProperties().put("contextPath", "contextPath1");
		serverEndpointConfig.getUserProperties().put("endpointPath", "endpointPath1");

		try {
			// install websocket endpoint
			serverContainer.addEndpoint(serverEndpointConfig);
		} catch (DeploymentException e) {
			e.printStackTrace();
		}
	}

	protected void addWebSocketClock(ServerContainer serverContainer) {

	}

	public static void main(String[] args) {
		// final WebSocketServerMain main = new WebSocketServerMain(8080);
		final WebSocketServerMain main = new WebSocketServerMain(7001);
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				main.start();
			}
		});
		t1.start();
		// try {
		// Thread.sleep(5 * 1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// main.stop();
	}

}
