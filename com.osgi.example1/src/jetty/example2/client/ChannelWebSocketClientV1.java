package jetty.example2.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class ChannelWebSocketClientV1 {

	protected String userId;
	protected String channelId;
	protected CountDownLatch latch = new CountDownLatch(1);
	protected Session session;

	public ChannelWebSocketClientV1(String userId, String channelId) {
		this.userId = userId;
		this.channelId = channelId;
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		this.latch.countDown();
		System.out.println("[" + this.userId + "] [" + this.channelId + "] ChannelWebSocketClient.onOpen() Connected to server");
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("[" + this.userId + "] [" + this.channelId + "] ChannelWebSocketClient.onMessage() message = " + message);
	}

	@OnClose
	public void onClose(CloseReason reason, Session session) {
		System.out.println("[" + this.userId + "] [" + this.channelId + "] ChannelWebSocketClient.onClose() Closing a WebSocket due to: " + reason.getReasonPhrase());
	}

	public CountDownLatch getLatch() {
		return this.latch;
	}

	public void sendMessage(String str) {
		try {
			this.session.getBasicRemote().sendText(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
