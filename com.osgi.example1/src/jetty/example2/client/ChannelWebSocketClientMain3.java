package jetty.example2.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import javax.xml.bind.DatatypeConverter;

public class ChannelWebSocketClientMain3 {

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

		ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
			@Override
			public void beforeRequest(Map<String, List<String>> headers) {
				headers.put("Authorization", Arrays.asList("Basic " + DatatypeConverter.printBase64Binary("username:password".getBytes())));
			}
		};
		ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create().configurator(configurator).build();

		ChannelWebSocketClientV2 webSocketClient = new ChannelWebSocketClientV2(userId, channelId);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(webSocketClient, clientConfig, uri);
		// container.connectToServer(webSocketClient, uri);

		if (sendClientMessages) {
			webSocketClient.getLatch().await();
			for (int i = 0; i < 100; i++) {
				try {
					Thread.sleep(10 * 1000);
					webSocketClient.sendMessage("Hello from websocket client '" + userId + "' for channel '" + channelId + "'");

				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			ChannelWebSocketClientMain3 main = new ChannelWebSocketClientMain3();
			main.connect("userC", "room1", false);

			Thread.sleep(5 * 60 * 1000);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
