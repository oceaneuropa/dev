package org.orbit.infra.runtime.datatube.service;

import java.io.IOException;

import org.orbit.infra.api.datacast.ChannelMetadata;

public interface RuntimeChannel extends MessageListenerSupport {

	ChannelMetadata getChannelMetadata();

	void setChannelMetadata(ChannelMetadata channelMetadata);

	int onMessage(String senderId, String message) throws IOException;

	void dispose();

}
