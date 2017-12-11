package org.orbit.infra.runtime.channel.service;

import java.util.List;

import org.orbit.infra.model.channel.ChannelException;

public interface Channel {

	int inbound(String senderId, String message) throws ChannelException;

	boolean addOutboundListener(OutboundListener listener);

	boolean removeOutboundListener(OutboundListener listener);

	List<OutboundListener> getOutboundListeners();

	void dispose();

}
