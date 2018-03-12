package org.orbit.infra.runtime.channel.service;

import java.util.List;

import org.orbit.infra.model.channel.ChannelException;
import org.origin.common.rest.server.WebServiceAware;

public interface ChannelService extends WebServiceAware {

	// String getNamespace();

	String getName();

	String getHostURL();

	String getContextRoot();

	String getHttpPort();

	int inbound(String channelId, String senderId, String message) throws ChannelException;

	boolean addOutboundListener(String channelId, OutboundListener listener);

	boolean removeOutboundListener(String channelId, OutboundListener listener);

	List<OutboundListener> getOutboundListeners(String channelId);

	void dispose();

}
