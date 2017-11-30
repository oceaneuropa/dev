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

	private static Logger LOG = LoggerFactory.getLogger(MyEndpoint1.class);

	public MyEndpoint1() {
		LOG.debug("MyEndpoint1()");
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("myPathParam") String myPathParam) {
		LOG.debug("OnOpen() " + toString() + " (" + hashCode() + ")");
		LOG.debug("\tmyPathParam = " + myPathParam);
	}

	@OnMessage
	public void onMessage(String message, Session session, @PathParam("myPathParam") String myPathParam) {
		LOG.debug("onMessage() " + toString() + " (" + hashCode() + ")");
		LOG.debug("\tmyPathParam = " + myPathParam);
		LOG.debug("\tmessage = " + message);
		try {
			session.getBasicRemote().sendText(message.toUpperCase());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("myPathParam") String myPathParam) {
		LOG.debug("onClose() " + toString() + " (" + hashCode() + ")");
		LOG.debug("\tmyPathParam = " + myPathParam);
	}

}

// SessionHelper.INSTANCE.printUserProperties(session);
