package org.orbit.infra.api.channel;

import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface Channels extends IAdaptable {

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	boolean ping();

	boolean send(String channelId, String senderId, String message) throws ClientException;

	boolean close() throws ClientException;

}
