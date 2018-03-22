package org.orbit.infra.runtime.channel.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.channel.ChannelException;
import org.orbit.infra.runtime.InfraConstants;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ChannelServiceImpl implements ChannelService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected String name;
	protected String hostURL;
	protected String contextRoot;
	protected String httpPort;
	protected Map<String, Channel> channelMap;
	protected ServiceRegistration<?> serviceRegistry;
	protected Map<Object, Object> properties;

	/**
	 * 
	 * @param initProperties
	 */
	public ChannelServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.channelMap = Collections.synchronizedMap(new HashMap<String, Channel>());
		this.properties = new HashMap<Object, Object>();
	}

	@Override
	public String getName() {
		return (String) this.properties.get(InfraConstants.COMPONENT_CHANNEL_NAME);
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(InfraConstants.COMPONENT_CHANNEL_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(InfraConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		return (String) this.properties.get(InfraConstants.COMPONENT_CHANNEL_CONTEXT_ROOT);
	}

	@Override
	public String getHttpPort() {
		return (String) this.properties.get(InfraConstants.COMPONENT_CHANNEL_HTTP_PORT);
	}

	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			configProps.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.COMPONENT_CHANNEL_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.COMPONENT_CHANNEL_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.COMPONENT_CHANNEL_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.COMPONENT_CHANNEL_HTTP_PORT);

		update(configProps);

		// Register service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(ChannelService.class, this, props);
	}

	public synchronized void update(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	@Override
	public int inbound(String channelId, String senderId, String message) throws ChannelException {
		Channel channel = getChannel(channelId);
		return channel.inbound(senderId, message);
	}

	public synchronized Channel getChannel(String channelId) {
		Channel channel = this.channelMap.get(channelId);
		if (channel == null) {
			channel = new ChannelImpl();
			this.channelMap.put(channelId, channel);
		}
		return channel;
	}

	public Channel removeChannel(String channelId) {
		Channel channel = this.channelMap.remove(channelId);
		if (channel != null) {
			channel.dispose();
		}
		return channel;
	}

	@Override
	public boolean addOutboundListener(String channelId, OutboundListener listener) {
		Channel channel = getChannel(channelId);
		return channel.addOutboundListener(listener);
	}

	@Override
	public boolean removeOutboundListener(String channelId, OutboundListener listener) {
		Channel channel = getChannel(channelId);
		return channel.removeOutboundListener(listener);
	}

	@Override
	public List<OutboundListener> getOutboundListeners(String channelId) {
		Channel channel = getChannel(channelId);
		List<OutboundListener> listeners = channel.getOutboundListeners();
		if (listeners == null) {
			listeners = Collections.emptyList();
		}
		return listeners;
	}

	@Override
	public synchronized void dispose() {
		for (Iterator<String> itor = this.channelMap.keySet().iterator(); itor.hasNext();) {
			String channelId = itor.next();
			removeChannel(channelId);
		}
		this.channelMap.clear();
	}

}

// protected String namespace;
// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.COMPONENT_CHANNEL_NAMESPACE);
// @Override
// public String getNamespace() {
// return (String) this.properties.get(InfraConstants.COMPONENT_CHANNEL_NAMESPACE);
// }
