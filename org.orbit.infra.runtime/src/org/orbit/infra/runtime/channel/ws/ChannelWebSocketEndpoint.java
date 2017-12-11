package org.orbit.infra.runtime.channel.ws;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.orbit.infra.runtime.channel.service.ChannelService;
import org.orbit.infra.runtime.channel.service.OutboundListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For one websocket client, one ChannelWebSocketEndpoint instance is created by websocket server.
 * 
 * @see http://www.tomitribe.com/blog/2015/05/websocket-and-lock-it/
 * 
 *      Websocket endpoint authorization
 * 
 */
@ServerEndpoint(value = "/channel/{channelId}")
public class ChannelWebSocketEndpoint implements OutboundListener {

	private static Logger LOG = LoggerFactory.getLogger(ChannelWebSocketEndpoint.class);

	public static final String getPath() {
		return "/channel/{channelId}";
	}

	protected Session session;

	public ChannelWebSocketEndpoint() {
		LOG.info("ChannelWebSocketEndpoint()");
		LOG.info("\ttoString() = " + this.toString());
		LOG.info("\thashCode() = " + String.valueOf(this.hashCode()));
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("channelId") String channelId) {
		LOG.info(this + ".onOpen() channelId = " + channelId);

		this.session = session;
		ChannelService service = getService(session);

		LOG.info("\tsession = " + session);
		LOG.info("\tservice = " + service);
		LOG.info("\tUserProperties:");
		Map<String, Object> userProps = session.getUserProperties();
		for (Iterator<String> itor = userProps.keySet().iterator(); itor.hasNext();) {
			String propName = itor.next();
			Object propValue = userProps.get(propName);
			LOG.info("\t\t" + propName + " = " + propValue);
		}

		registerOutboundListener(session, channelId);
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
			ChannelService service = getService(session);
			service.inbound(channelId, "n/a", message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("channelId") String channelId) {
		LOG.info(this + ".onClose() channelId = " + channelId);

		unregisterOutboundListener(session, channelId);
	}

	protected ChannelService getService(Session session) {
		ChannelService service = null;
		Object obj = session.getUserProperties().get(ChannelService.class.getName());
		if (obj instanceof ChannelService) {
			service = (ChannelService) obj;
		}
		if (service == null) {
			throw new RuntimeException("ChannelService is not available.");
		}
		return service;
	}

	@Override
	public void outbound(String senderId, String message) {
		LOG.info("outbound() senderId = '" + senderId + "', message = '" + message + "'");
		// LOG.info("\tsession = " + session);
		if (this.session != null) {
			try {
				this.session.getBasicRemote().sendText(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void registerOutboundListener(Session session, String channelId) {
		LOG.info("registerOutboundListener() channelId = " + channelId);

		ChannelService channelService = getService(session);
		if (channelService != null) {
			boolean found = false;
			List<OutboundListener> listeners = channelService.getOutboundListeners(channelId);
			for (OutboundListener listener : listeners) {
				if (listener == this) {
					found = true;
					break;
				}
			}
			if (!found) {
				channelService.addOutboundListener(channelId, this);
			}
		}
	}

	protected void unregisterOutboundListener(Session session, String channelId) {
		LOG.info("unregisterOutboundListener() channelId = " + channelId);

		ChannelService channelService = getService(session);
		if (channelService != null) {
			OutboundListener foundListener = null;
			List<OutboundListener> listeners = channelService.getOutboundListeners(channelId);
			for (OutboundListener listener : listeners) {
				if (listener == this) {
					foundListener = listener;
					break;
				}
			}
			if (foundListener != null) {
				channelService.removeOutboundListener(channelId, foundListener);
			}
		}
	}

}
