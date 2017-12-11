package org.orbit.infra.runtime.channel.service;

public interface OutboundListener {

	void outbound(String senderId, String message);

}
