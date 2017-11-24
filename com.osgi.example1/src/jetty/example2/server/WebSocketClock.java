package jetty.example2.server;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * https://yakovfain.com/2014/12/29/pushing-data-to-multiple-websocket-clients-from-a-java-server/
 * 
 */
@ServerEndpoint("/clock")
public class WebSocketClock {

	public ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
	protected Set<Session> allSessions;
	protected DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@OnOpen
	public void showTime(Session session) {
		this.allSessions = session.getOpenSessions();

		// start the scheduler on the very first connection
		// to call sendTimeToAll every second
		if (this.allSessions.size() == 1) {
			this.timer.scheduleAtFixedRate(() -> sendTimeToAll(session), 0, 1, TimeUnit.SECONDS);
		}
	}

	private void sendTimeToAll(Session session) {
		this.allSessions = session.getOpenSessions();
		for (Session sess : this.allSessions) {
			try {
				sess.getBasicRemote().sendText("Local time: " + LocalTime.now().format(timeFormatter));
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}
	}

}
