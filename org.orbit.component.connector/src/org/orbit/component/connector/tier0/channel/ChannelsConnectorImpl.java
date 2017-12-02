package org.orbit.component.connector.tier0.channel;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.tier0.channel.Channels;
import org.orbit.component.api.tier0.channel.ChannelsConnector;
import org.orbit.component.connector.OrbitConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ChannelsConnectorImpl implements ChannelsConnector {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, Channels> channelMap;

	public ChannelsConnectorImpl() {
		this.channelMap = new HashMap<String, Channels>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(ChannelsConnector.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		this.channelMap.clear();
	}

	@Override
	public synchronized Channels getService(Map<Object, Object> properties) {
		Channels channel = null;
		String url = (String) properties.get(OrbitConstants.CHANNEL_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.CHANNEL_CONTEXT_ROOT);
		if (url != null && contextRoot != null) {
			String key = url + KEY_PARTS_SEPARATOR + contextRoot;
			channel = this.channelMap.get(key);
			if (channel == null) {
				channel = new ChannelsImpl(properties);
				this.channelMap.put(key, channel);
			}
		}
		return channel;
	}

}
