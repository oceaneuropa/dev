package org.orbit.infra.connector.channel;

import java.util.Map;

import org.orbit.infra.api.channel.Channels;
import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class ChannelsConnector extends ServiceConnector<Channels> implements IConnectorActivator {

	public static final String ID = "org.orbit.infra.connector.ChannelsConnector";

	public ChannelsConnector() {
		super(Channels.class);
	}

	@Override
	protected Channels create(Map<String, Object> properties) {
		return new ChannelsImpl(this, properties);
	}

}
