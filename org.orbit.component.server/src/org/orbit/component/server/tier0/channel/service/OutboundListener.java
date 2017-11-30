package org.orbit.component.server.tier0.channel.service;

public interface OutboundListener {

	void outbound(String senderId, String message);

}
