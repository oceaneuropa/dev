package jetty.example2.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * https://examples.javacodegeeks.com/enterprise-java/jetty/jetty-websocket-example/
 *
 */
@ClientEndpoint
public class ToUpper356ClientSocket {

	private CountDownLatch latch = new CountDownLatch(1);
	private Session session;

	@OnOpen
	public void onOpen(Session session) {
		System.out.println(getClass().getSimpleName() + ".onOpen() Connected to server");
		this.session = session;
		latch.countDown();
	}

	@OnMessage
	public void onText(String message, Session session) {
		System.out.println(getClass().getSimpleName() + ".onText() Message received from server:" + message);
	}

	@OnClose
	public void onClose(CloseReason reason, Session session) {
		System.out.println(getClass().getSimpleName() + ".onClose() Closing a WebSocket due to: " + reason.getReasonPhrase());
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public void sendMessage(String str) {
		try {
			this.session.getBasicRemote().sendText(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
