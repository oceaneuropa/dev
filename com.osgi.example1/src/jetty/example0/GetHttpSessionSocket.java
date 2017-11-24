package jetty.example0;

import java.io.IOException;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * https://stackoverflow.com/questions/28188172/how-to-access-client-hostname-http-headers-etc-from-a-java-websocket-server
 * 
 */
@ServerEndpoint(value = "/example", configurator = ServletAwareConfigurator.class)
public class GetHttpSessionSocket {

	private Session wsSession;
	private String sClientIP;

	@OnOpen
	public void open(Session session, EndpointConfig config) {
		this.wsSession = session;
		this.sClientIP = (String) config.getUserProperties().get("clientip");
	}

	@OnMessage
	public void echo(String msg) throws IOException {
		wsSession.getBasicRemote().sendText(msg);
	}

}
