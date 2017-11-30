package org.orbit.component.server.tier0.channel.service;

import java.util.List;

import org.orbit.component.model.tier0.channel.ChannelException;

public interface Channel {

	int inbound(String senderId, String message) throws ChannelException;

	boolean addOutboundListener(OutboundListener listener);

	boolean removeOutboundListener(OutboundListener listener);

	List<OutboundListener> getOutboundListeners();

	void dispose();

}
