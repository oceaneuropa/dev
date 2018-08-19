package org.orbit.infra.connector.channel;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.channel.ChannelClient;
import org.orbit.infra.model.channel.ChannelMessageDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;

public class ChannelsImpl implements ChannelClient {

	protected Map<String, Object> properties;
	protected ChannelWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	public ChannelsImpl(ServiceConnector<ChannelClient> connector, Map<String, Object> properties) {
		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
		this.properties = checkProperties(properties);
		initClient();
	}

	protected Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public boolean close() throws ClientException {
		@SuppressWarnings("unchecked")
		ServiceConnector<ChannelClient> connector = getAdapter(ServiceConnector.class);
		if (connector != null) {
			return connector.close(this);
		}
		return false;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void update(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected void initClient() {
		WSClientConfiguration config = WSClientConfiguration.create(this.properties);
		this.client = new ChannelWSClient(config);
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

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}
