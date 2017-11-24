package jetty.example3;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

//invoke at : http://localhost:7101/CinemaMonitor/resources/cinemaevent

@Path("cinemaevent")
public class MovieEvent {

	private MovieEventSocketClient client;

	private final String webSocketAddress = "ws://localhost:7101/CinemaMonitor/cinemaSocket";

	public MovieEvent() {
	}

	private void initializeWebSocket() throws URISyntaxException {
		// ws://localhost:7101/CinemaMonitor/cinemaSocket/
		System.out.println("REST service: open websocket client at " + webSocketAddress);
		client = new MovieEventSocketClient(new URI(webSocketAddress + "/0"));
		// add listener
		client.addMessageHandler(new MovieEventSocketClient.MessageHandler() {
			public void handleMessage(String message) {
				System.out.println("messagehandler in REST service - process message " + message);
			}
		});
	}

	private void sendMessageOverSocket(String message) {
		if (client == null) {
			try {
				initializeWebSocket();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		client.sendMessage(message);

	}

	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	public String postMovieEvent(@Context Request request, String json) {
		System.out.println("received event:" + json);
		sendMessageOverSocket(json);
		return "event received " + json;
	}

	@GET
	@Produces("text/plain")
	public String getMovieEvent(@Context Request request) {
		return "nothing to report from getMovieEvent";
	}
}
