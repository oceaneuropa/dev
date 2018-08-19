package other.orbit.infra.connector.channel;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.channel.ChannelClient;
import org.orbit.infra.connector.InfraConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ChannelsConnectorImplV1 /* implements ChannelsConnector*/ {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, ChannelClient> serviceMap;

	public ChannelsConnectorImplV1() {
		this.serviceMap = new HashMap<String, ChannelClient>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		// this.serviceRegistration = bundleContext.registerService(ChannelsConnector.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		this.serviceMap.clear();
	}

	// @Override
	public synchronized ChannelClient getService(Map<String, Object> properties) {
		ChannelClient channel = null;
		String url = (String) properties.get(InfraConstants.CHANNEL_HOST_URL);
		String contextRoot = (String) properties.get(InfraConstants.CHANNEL_CONTEXT_ROOT);
		if (url != null && contextRoot != null) {
			String key = url + KEY_PARTS_SEPARATOR + contextRoot;
			channel = this.serviceMap.get(key);
			if (channel == null) {
				channel = new ChannelsImplV1(null, properties);
				this.serviceMap.put(key, channel);
			}
		}
		return channel;
	}

}
