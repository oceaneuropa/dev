package org.orbit.infra.runtime.datatube.ws;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.orbit.infra.runtime.datatube.service.Channel;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.service.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 1. For one websocket client, one ChannelWebSocketEndpoint instance is created by websocket server.
 * 
 * 2. Websocket endpoint authorization @see http://www.tomitribe.com/blog/2015/05/websocket-and-lock-it/
 * 
 */
@ServerEndpoint(value = "/channel/{channelId}")
public class DataTubeWebSocketServerEndpoint implements MessageListener {

	private static Logger LOG = LoggerFactory.getLogger(DataTubeWebSocketServerEndpoint.class);

	protected Session session;

	public DataTubeWebSocketServerEndpoint() {
		LOG.info("ChannelServerEndpoint()");
		LOG.info("\ttoString() = " + this.toString());
		LOG.info("\thashCode() = " + String.valueOf(this.hashCode()));
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	protected DataTubeService getService(Session session) {
		DataTubeService service = null;
		Object obj = session.getUserProperties().get(DataTubeService.class.getName());
		if (obj instanceof DataTubeService) {
			service = (DataTubeService) obj;
		}
		if (service == null) {
			throw new RuntimeException("ChannelService is not available.");
		}
		return service;
	}

	/**
	 * 
	 * @param session
	 * @param channelId
	 */
	protected void registerMessageListener(Session session, String channelId) {
		LOG.info("registerMessageListener() channelId = " + channelId);

		DataTubeService channelService = getService(session);
		if (channelService != null) {

			Channel channel = channelService.getChannel(channelId);
			if (channel != null) {
				boolean found = false;

				List<MessageListener> listeners = channel.getMessageListeners();
				for (MessageListener listener : listeners) {
					if (listener == this) {
						found = true;
						break;
					}
				}
				if (!found) {
					channel.addMessageListener(this);
				}
			}
		}
	}

	/**
	 * 
	 * @param session
	 * @param channelId
	 */
	protected void unregisterMessageListener(Session session, String channelId) {
		LOG.info("unregisterMessageListener() channelId = " + channelId);

		DataTubeService channelService = getService(session);
		if (channelService != null) {

			Channel channel = channelService.getChannel(channelId);
			if (channel != null) {
				channel.removeMessageListener(this);
			}
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("channelId") String channelId) {
		LOG.info(this + ".onOpen() channelId = " + channelId);

		this.session = session;
		DataTubeService service = getService(session);

		LOG.info("\tsession = " + session);
		LOG.info("\tservice = " + service);
		LOG.info("\tUserProperties:");
		Map<String, Object> userProps = session.getUserProperties();
		for (Iterator<String> itor = userProps.keySet().iterator(); itor.hasNext();) {
			String propName = itor.next();
			Object propValue = userProps.get(propName);
			LOG.info("\t\t" + propName + " = " + propValue);
		}

		registerMessageListener(session, channelId);
	}

	@OnMessage
	public void onMessage(String message, Session session, @PathParam("channelId") String channelId) {
		LOG.info(this + ".onMessage() channelId = " + channelId);
		LOG.info("\tmessage = " + message);

		LOG.info("\tsession = " + session);
		LOG.info("\tUserProperties:");
		Map<String, Object> userProps = session.getUserProperties();
		for (Iterator<String> itor = userProps.keySet().iterator(); itor.hasNext();) {
			String propName = itor.next();
			Object propValue = userProps.get(propName);
			LOG.info("\t\t" + propName + " = " + propValue);
		}

		// Example UserProperties:
		// ------------------------------------------------------------------------------------------------------------
		// org.orbit.component.server.tier0.channel.service.ChannelService = org.orbit.component.server.tier0.channel.service.ChannelServiceImpl@c901749
		// org.orbit.service.websocket.WebSocketEndpoint = org.orbit.service.websocket.impl.WebSocketEndpointImpl@77b7cd8
		// javax.websocket.endpoint.localAddress = /127.0.0.1:7001
		// javax.websocket.endpoint.remoteAddress = /127.0.0.1:63110
		// javax.websocket.upgrade.locales = []
		// ------------------------------------------------------------------------------------------------------------
		try {
			DataTubeService service = getService(session);

			Channel channel = service.getChannel(channelId);
			if (channel != null) {
				channel.onMessage("unknown-sender", message);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("channelId") String channelId) {
		LOG.info(this + ".onClose() channelId = " + channelId);

		unregisterMessageListener(session, channelId);
	}

	@OnError
	public void onError(Session session, @PathParam("channelId") String channelId) {
		LOG.info(this + ".onError() channelId = " + channelId);
	}

	/** Implement ChannelMessageListener */
	@Override
	public void onMessage(String senderId, String message) {
		LOG.info("onMessage() senderId = '" + senderId + "', message = '" + message + "'");
		if (this.session != null) {
			try {
				// Broadcast the message (to the browser)
				this.session.getBasicRemote().sendText(message);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
