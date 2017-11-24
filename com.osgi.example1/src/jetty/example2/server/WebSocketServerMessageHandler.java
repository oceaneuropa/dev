package jetty.example2.server;

import java.io.IOException;
import java.io.Reader;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class WebSocketServerMessageHandler implements MessageHandler.Whole<Reader> {

	private Session session;

	public WebSocketServerMessageHandler(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return this.session;
	}

	@Override
	public void onMessage(Reader reader) {
		System.out.println("Incoming message on session " + this.getSession().getId());

		// // read properties
		// WebSocketTransport webSocketTransport = (WebSocketTransport) this.getSession().getUserProperties().get("webSocketTransport");
		// String contextPath = (String) this.getSession().getUserProperties().get("contextPath");
		// String endpointPath = (String) this.getSession().getUserProperties().get("endpointPath");

		// // execute the transport
		// WebSocketTransportRequest request = WebSocketTransportRequest.create(this, this.getSession(), contextPath, endpointPath, reader);
		// WebSocketTransportResponse response = WebSocketTransportResponse.create(this, this.getSession());

		try {
			// webSocketTransport.execute(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				System.out.println("I/O exception: " + ex.getMessage());
				this.getSession().close(new CloseReason(CloseCodes.UNEXPECTED_CONDITION, "I/O exception: " + ex.getMessage()));
			} catch (IOException ex2) {
				System.out.println("Cannot close session: " + ex.getMessage());
				return;
			}
		}
	}

}
