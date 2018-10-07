package other.orbit.infra.api.channel;

import java.util.Map;

import org.orbit.infra.api.datatube.DataTubeClient;

public interface ChannelsConnector {

	DataTubeClient getService(Map<Object, Object> properties);

}
