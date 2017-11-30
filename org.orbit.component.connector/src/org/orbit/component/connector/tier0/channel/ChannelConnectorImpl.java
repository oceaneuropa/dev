package org.orbit.component.connector.tier0.channel;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.tier0.channel.ChannelClient;
import org.orbit.component.api.tier0.channel.ChannelConnector;
import org.orbit.component.connector.OrbitConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ChannelConnectorImpl implements ChannelConnector {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, ChannelClient> channelClientMap;

	public ChannelConnectorImpl() {
		this.channelClientMap = new HashMap<String, ChannelClient>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(ChannelConnector.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		this.channelClientMap.clear();
	}

	@Override
	public synchronized ChannelClient getChannelClient(Map<Object, Object> properties) {
		ChannelClient channelClient = null;
		String url = (String) properties.get(OrbitConstants.CHANNEL_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.CHANNEL_CONTEXT_ROOT);
		if (url != null && contextRoot != null) {
			String key = url + KEY_PARTS_SEPARATOR + contextRoot;
			channelClient = this.channelClientMap.get(key);
			if (channelClient == null) {
				channelClient = new ChannelClientImpl(properties);
				this.channelClientMap.put(key, channelClient);
			}
		}
		return channelClient;
	}

}
