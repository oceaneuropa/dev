package jetty.example3;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@ServerEndpoint("/cinemaSocket/{client-id}")
public class CinemaEventSocketMediator {

	private Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public String onMessage(String message, Session session, @PathParam("client-id") String clientId) {
		try {
			JSONObject jObj = new JSONObject(message);
			System.out.println("received message from client " + clientId);
			for (Session peer : this.peers) {
				try {
					peer.getBasicRemote().sendText(message);
					System.out.println("send message to peer ");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "message was received by socket mediator and processed: " + message;
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("client-id") String clientId) {
		System.out.println("mediator: opened websocket channel for client " + clientId);
		this.peers.add(session);
		try {
			session.getBasicRemote().sendText("good to be in touch");
		} catch (IOException e) {
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("client-id") String clientId) {
		System.out.println("mediator: closed websocket channel for client " + clientId);
		this.peers.remove(session);
	}

}
