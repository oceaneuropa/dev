package jetty.example2.server;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.orbit.service.websocket.WebSocketEndpoint;

/**
 * https://examples.javacodegeeks.com/enterprise-java/jetty/jetty-websocket-example/
 *
 */
@ServerEndpoint("/jsr356toUpper1")
public class ToUpper356Socket {

	protected boolean isActivated(Session session) {
		WebSocketEndpoint endpoint = (WebSocketEndpoint) session.getUserProperties().get(WebSocketEndpoint.class.getName());
		if (endpoint != null) {
			return endpoint.isActivated();
		}
		return false;
	}

	@OnOpen
	public void onOpen(Session session) {
		println("onOpen() WebSocket opened: " + session.getId());
		// SessionHelper.INSTANCE.print(session);
		if (!isActivated(session)) {
			println("websocket is not activated.");
		}
	}

	@OnMessage
	public void onMessage(String text, Session session) throws IOException {
		println("onMessage() Message received: " + text + ", sessionId: " + session.getId());
		if (!isActivated(session)) {
			println("websocket is not activated.");
		}

		String v1 = (String) session.getUserProperties().get("webSocketTransport");
		String v2 = (String) session.getUserProperties().get("contextPath");
		String v3 = (String) session.getUserProperties().get("endpointPath");
		String v4 = (String) session.getUserProperties().get("context_root");
		System.out.println("v1 = " + v1);
		System.out.println("v2 = " + v2);
		System.out.println("v3 = " + v3);
		System.out.println("v4 = " + v4);

		session.getBasicRemote().sendText(text.toUpperCase());
	}

	@OnClose
	public void onClose(CloseReason reason, Session session) {
		println("onClose() Closing a WebSocket due to: " + reason.getReasonPhrase());
		if (!isActivated(session)) {
			println("websocket is not activated.");
		}
	}

	protected void println() {
		System.out.println(getClass().getSimpleName());
	}

	protected void println(String msg) {
		System.out.println(getClass().getSimpleName() + " - " + msg);
	}

}
