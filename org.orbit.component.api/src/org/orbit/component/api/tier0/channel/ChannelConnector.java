package org.orbit.component.api.tier0.channel;

import java.util.Map;

public interface ChannelConnector {

	ChannelClient getChannelClient(Map<Object, Object> properties);

}
