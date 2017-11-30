package jetty.example2.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://www.programcreek.com/java-api-examples/index.php?api=javax.websocket.WebSocketContainer
 *
 */
@ClientEndpoint
public class ChannelWebSocketClientV2 extends Endpoint {

	private static Logger LOG = LoggerFactory.getLogger(ChannelWebSocketClientV2.class);

	protected String userId;
	protected String channelId;
	protected CountDownLatch latch = new CountDownLatch(1);
	protected Session session;

	public ChannelWebSocketClientV2(String userId, String channelId) {
		this.userId = userId;
		this.channelId = channelId;
	}

	@Override
	public void onOpen(final Session session, EndpointConfig endpointConfig) {
		LOG.info("ChannelWebSocketClientV2.onOpen() [" + this.userId + "] [" + this.channelId + "] Connected to server.");
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				LOG.info("ChannelWebSocketClient.MessageHandler.onMessage() [" + userId + "] [" + channelId + "] message = " + message);
			}
		});
		this.latch.countDown();
	}

	@Override
	public void onClose(Session sess, CloseReason reason) {
		LOG.info("ChannelWebSocketClientV2.onClose() [" + this.userId + "] [" + this.channelId + "] Closing a WebSocket due to: " + reason.getReasonPhrase());
		session = null;
	}

	@Override
	public void onError(Session session, Throwable thr) {
		LOG.info("ChannelWebSocketClientV2.onError() [" + this.userId + "] [" + this.channelId + "] Error due to: " + thr.getMessage());
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
