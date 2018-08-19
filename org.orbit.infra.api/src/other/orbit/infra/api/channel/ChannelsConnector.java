package other.orbit.infra.api.channel;

import java.util.Map;

import org.orbit.infra.api.channel.ChannelClient;

public interface ChannelsConnector {

	ChannelClient getService(Map<Object, Object> properties);

}
