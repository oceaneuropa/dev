package org.orbit.infra.runtime.datatube.service;

import org.orbit.infra.model.channel.ChannelException;

public interface Channel extends MessageListenerSupport {

	int onMessage(String senderId, String message) throws ChannelException;

	void dispose();

}
