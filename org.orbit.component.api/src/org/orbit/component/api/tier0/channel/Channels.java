package org.orbit.component.api.tier0.channel;

import java.util.Map;

import org.origin.common.rest.client.ClientException;

public interface Channels {

	Map<Object, Object> getProperties();

	void update(Map<Object, Object> properties);

	boolean ping();

	boolean send(String channelId, String senderId, String message) throws ClientException;

}
