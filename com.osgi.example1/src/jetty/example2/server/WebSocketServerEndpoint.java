package jetty.example2.server;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class WebSocketServerEndpoint extends Endpoint {

	@Override
	public void onOpen(Session session, EndpointConfig config) {
	}

	@Override
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("WebSocket session " + session.getId() + " closed.");
		// // read properties
		// WebSocketTransport webSocketTransport = (WebSocketTransport) session.getUserProperties().get("webSocketTransport");
		// // unregister session
		// webSocketTransport.unregisterSession(session);
	}

	@Override
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
		System.out.println("WebSocket session " + session.getId() + " problem: " + throwable.getMessage());
	}

}
