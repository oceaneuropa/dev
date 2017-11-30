package jetty.example2.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

public class ChannelWebSocketClientMain2 {

	/**
	 * 
	 * @param userId
	 * @param channelId
	 * @param sendClientMessages
	 * @throws DeploymentException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 */
	public void connect(String userId, String channelId, boolean sendClientMessages) throws DeploymentException, IOException, URISyntaxException, InterruptedException {
		URI uri = new URI("ws://localhost:7001/channel/" + channelId);

		ChannelWebSocketClientV1 webSocketClient = new ChannelWebSocketClientV1(userId, channelId);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(webSocketClient, uri);

		if (sendClientMessages) {
			webSocketClient.getLatch().await();
			for (int i = 0; i < 100; i++) {
				try {
					Thread.sleep(20 * 1000);
					webSocketClient.sendMessage("Hello from websocket client '" + userId + "' for channel '" + channelId + "'");

				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			ChannelWebSocketClientMain2 main = new ChannelWebSocketClientMain2();
			main.connect("userB", "room1", true);

			Thread.sleep(5 * 60 * 1000);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
