package jetty.example2.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

/**
 * https://examples.javacodegeeks.com/enterprise-java/jetty/jetty-websocket-example/
 *
 */
public class ToUpper356ClientSocketMain {

	public void send1() throws DeploymentException, IOException, URISyntaxException, InterruptedException {
		// String dest = "ws://localhost:8080/orbit/jsr356toUpper";
		String dest = "ws://localhost:7001/orbit/jsr356toUpper";
		ToUpper356ClientSocket socket = new ToUpper356ClientSocket();
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(socket, new URI(dest));

		socket.getLatch().await();
		socket.sendMessage("echo356");
		socket.sendMessage("test356");
	}

	public void send2(String param) throws DeploymentException, IOException, URISyntaxException, InterruptedException {
		String dest = "ws://localhost:7001/websocket/" + param;
		ToUpper356ClientSocket socket = new ToUpper356ClientSocket();
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(socket, new URI(dest));

		socket.getLatch().await();
		socket.sendMessage("my message from send2 " + param);
	}

	public static void main(String[] args) {
		try {
			ToUpper356ClientSocketMain main = new ToUpper356ClientSocketMain();

			for (int i = 0; i < 3; i++) {
				// main.send1();
				main.send2("user" + i);

				Thread.sleep(10 * 1000);
			}

			Thread.sleep(120 * 1000);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
