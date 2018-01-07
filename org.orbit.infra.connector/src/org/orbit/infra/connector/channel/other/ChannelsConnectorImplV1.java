package org.orbit.infra.connector.channel.other;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.channel.Channels;
import org.orbit.infra.connector.InfraConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ChannelsConnectorImplV1 /* implements ChannelsConnector*/ {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, Channels> serviceMap;

	public ChannelsConnectorImplV1() {
		this.serviceMap = new HashMap<String, Channels>();
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
	public synchronized Channels getService(Map<String, Object> properties) {
		Channels channel = null;
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
