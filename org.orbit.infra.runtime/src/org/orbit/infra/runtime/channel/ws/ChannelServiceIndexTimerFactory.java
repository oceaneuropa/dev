package org.orbit.infra.runtime.channel.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.channel.service.ChannelService;

public class ChannelServiceIndexTimerFactory implements ServiceIndexTimerFactory<ChannelService> {

	@Override
	public ChannelServiceIndexTimer create(IndexProvider indexProvider, ChannelService service) {
		return new ChannelServiceIndexTimer(indexProvider, service);
	}

}
