package other.orbit.infra.api.channel;

import java.util.Map;

import org.orbit.infra.api.channel.Channels;

public interface ChannelsConnector {

	Channels getService(Map<Object, Object> properties);

}
