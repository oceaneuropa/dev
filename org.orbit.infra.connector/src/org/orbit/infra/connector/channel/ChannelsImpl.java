package org.orbit.infra.connector.channel;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.channel.Channels;
import org.orbit.infra.connector.OrbitConstants;
import org.orbit.infra.model.channel.ChannelMessageDTO;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.StringUtil;

public class ChannelsImpl implements Channels {

	protected Map<Object, Object> properties;
	protected ChannelWSClient client;

	public ChannelsImpl(Map<Object, Object> properties) {
		this.properties = checkProperties(properties);
		init();
	}

	protected Map<Object, Object> checkProperties(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		return properties;
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void update(Map<Object, Object> properties) {
		properties = checkProperties(properties);

		String oldUrl = (String) this.properties.get(OrbitConstants.CHANNEL_HOST_URL);
		String oldContextRoot = (String) this.properties.get(OrbitConstants.CHANNEL_CONTEXT_ROOT);

		this.properties.putAll(properties);

		String newUrl = (String) properties.get(OrbitConstants.CHANNEL_HOST_URL);
		String newContextRoot = (String) properties.get(OrbitConstants.CHANNEL_CONTEXT_ROOT);

		boolean reinit = false;
		if (!StringUtil.equals(oldUrl, newUrl) || !StringUtil.equals(oldContextRoot, newContextRoot)) {
			reinit = true;
		}
		if (reinit) {
			init();
		}
	}

	protected void init() {
		String url = (String) this.properties.get(OrbitConstants.CHANNEL_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.CHANNEL_CONTEXT_ROOT);
		ClientConfiguration clientConfig = ClientConfiguration.get(url, contextRoot, null, null);

		this.client = new ChannelWSClient(clientConfig);
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public boolean send(String channelId, String senderId, String message) throws ClientException {
		try {
			ChannelMessageDTO messageDTO = new ChannelMessageDTO();
			messageDTO.setChannelId(channelId);
			messageDTO.setSenderId(senderId);
			messageDTO.setMessage(message);

			int result = this.client.sendMessage(messageDTO);
			if (result > 0) {
				return true;
			}

		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

}
