package jetty.example2.server;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(value = "/websocket/{myPathParam}")
public class MyEndpoint1 {

	private Logger LOG = LoggerFactory.getLogger(MyEndpoint1.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("myPathParam") String myPathParam) {
		LOG.info("OnOpen");

		println("myPathParam = " + myPathParam);
		SessionHelper.INSTANCE.printUserProperties(session);
	}

	@OnClose
	public void onClose(Session session, @PathParam("myPathParam") String myPathParam) {
		LOG.info("OnClose");

		println("myPathParam = " + myPathParam);
		SessionHelper.INSTANCE.printUserProperties(session);
	}

	@OnMessage
	public void onMessage(String message, Session session, @PathParam("myPathParam") String myPathParam) {
		LOG.info("OnMessage: " + message);

		println("myPathParam = " + myPathParam);
		SessionHelper.INSTANCE.printUserProperties(session);

		try {
			session.getBasicRemote().sendText(message.toUpperCase());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void println() {
		System.out.println(getClass().getSimpleName());
	}

	protected void println(String msg) {
		System.out.println(getClass().getSimpleName() + " - " + msg);
	}

}
