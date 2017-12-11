package org.orbit.infra.api.channel;

import java.util.Map;

public interface ChannelsConnector {

	Channels getService(Map<Object, Object> properties);

}
