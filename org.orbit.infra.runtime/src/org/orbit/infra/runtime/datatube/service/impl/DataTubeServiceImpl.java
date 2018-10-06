package org.orbit.infra.runtime.datatube.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datatube.service.Channel;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DataTubeServiceImpl implements DataTubeService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected String name;
	protected String hostURL;
	protected String contextRoot;
	protected String httpPort;
	protected Map<String, Channel> channelMap;
	protected ServiceRegistration<?> serviceRegistry;
	protected Map<Object, Object> properties;
	protected ServiceEditPolicies wsEditPolicies;

	/**
	 * 
	 * @param initProperties
	 */
	public DataTubeServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.channelMap = Collections.synchronizedMap(new HashMap<String, Channel>());
		this.properties = new HashMap<Object, Object>();
	}

	@Override
	public String getName() {
		return (String) this.properties.get(InfraConstants.DATATUBE__NAME);
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(InfraConstants.DATATUBE__HOST_URL);
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
		return (String) this.properties.get(InfraConstants.DATATUBE__CONTEXT_ROOT);
	}

	@Override
	public String getWebSocketHttpPort() {
		return (String) this.properties.get(InfraConstants.DATATUBE__HTTP_PORT);
	}

	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			configProps.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__HTTP_PORT);

		update(configProps);

		// Register service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DataTubeService.class, this, props);
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
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	@Override
	public synchronized Channel[] getChannels() {
		return this.channelMap.values().toArray(new Channel[this.channelMap.size()]);
	}

	protected void checkChannelId(String channelId) {
		if (channelId == null || channelId.isEmpty()) {
			throw new IllegalArgumentException("channelId is empty.");
		}
	}

	@Override
	public synchronized boolean channelExists(String channelId) {
		checkChannelId(channelId);

		boolean exists = false;
		Channel channel = this.channelMap.get(channelId);
		if (channel != null) {
			exists = true;
		}
		return exists;
	}

	@Override
	public synchronized Channel getChannel(String channelId) {
		checkChannelId(channelId);

		Channel channel = this.channelMap.get(channelId);
		if (channel == null) {
			channel = new ChannelImpl();
			this.channelMap.put(channelId, channel);
		}

		return channel;
	}

	@Override
	public synchronized Channel removeChannel(String channelId) {
		checkChannelId(channelId);

		Channel channel = this.channelMap.remove(channelId);
		if (channel != null) {
			channel.dispose();
		}
		return channel;
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

// @Override
// public boolean addMessageListener(String channelId, ChannelMessageListener listener) {
// Channel channel = getChannel(channelId);
// return channel.addMessageListener(listener);
// }
//
// @Override
// public boolean removeOutboundListener(String channelId, ChannelMessageListener listener) {
// Channel channel = getChannel(channelId);
// return channel.removeMessageListener(listener);
// }
//
// @Override
// public List<ChannelMessageListener> getOutboundListeners(String channelId) {
// Channel channel = getChannel(channelId);
// List<ChannelMessageListener> listeners = channel.getMessageListeners();
// if (listeners == null) {
// listeners = Collections.emptyList();
// }
// return listeners;
// }
