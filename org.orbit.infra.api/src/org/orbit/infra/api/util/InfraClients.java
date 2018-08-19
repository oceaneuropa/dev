package org.orbit.infra.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.channel.ChannelClient;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClientProxy;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderProxy;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexServiceProxy;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfraClients implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(InfraClients.class);

	public static InfraClients INSTANCE = new InfraClients();

	public static InfraClients getInstance() {
		return INSTANCE;
	}

	protected ServiceConnectorAdapter<IndexProvider> indexProviderConnector;
	protected ServiceConnectorAdapter<IndexService> indexServiceConnector;
	protected ServiceConnectorAdapter<ExtensionRegistryClient> extensionRegistryConnector;
	protected ServiceConnectorAdapter<ChannelClient> channelsConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.indexProviderConnector = new ServiceConnectorAdapter<IndexProvider>(IndexProvider.class);
		this.indexProviderConnector.start(bundleContext);

		this.indexServiceConnector = new ServiceConnectorAdapter<IndexService>(IndexService.class);
		this.indexServiceConnector.start(bundleContext);

		this.extensionRegistryConnector = new ServiceConnectorAdapter<ExtensionRegistryClient>(ExtensionRegistryClient.class);
		this.extensionRegistryConnector.start(bundleContext);

		this.channelsConnector = new ServiceConnectorAdapter<ChannelClient>(ChannelClient.class);
		this.channelsConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void stop(final BundleContext bundleContext) {
		if (this.channelsConnector != null) {
			this.channelsConnector.stop(bundleContext);
			this.channelsConnector = null;
		}

		if (this.extensionRegistryConnector != null) {
			this.extensionRegistryConnector.stop(bundleContext);
			this.extensionRegistryConnector = null;
		}

		if (this.indexServiceConnector != null) {
			this.indexServiceConnector.stop(bundleContext);
			this.indexServiceConnector = null;
		}

		if (this.indexProviderConnector != null) {
			this.indexProviderConnector.stop(bundleContext);
			this.indexProviderConnector = null;
		}
	}

	/**
	 * 
	 * @param properties
	 * @param useProxy
	 * @return
	 */
	public IndexProvider getIndexProvider(Map<?, ?> properties, boolean useProxy) {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(WSClientConstants.URL);
		if (url == null) {
			url = (String) properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		}

		Map<String, Object> theProperties = new HashMap<String, Object>();
		theProperties.put(WSClientConstants.REALM, realm);
		theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		theProperties.put(WSClientConstants.URL, url);

		IndexProvider indexProvider = this.indexProviderConnector.getService(theProperties);
		if (indexProvider == null && useProxy) {
			// throw new RuntimeException("IndexProvider is not available.");
			indexProvider = new IndexProviderProxy(theProperties);
		}
		return indexProvider;
	}

	/**
	 * 
	 * @param properties
	 * @param useProxy
	 * @return
	 */
	public IndexService getIndexService(Map<?, ?> properties, boolean useProxy) {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(WSClientConstants.URL);
		if (url == null) {
			url = (String) properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		}

		Map<String, Object> theProperties = new HashMap<String, Object>();
		theProperties.put(WSClientConstants.REALM, realm);
		theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		theProperties.put(WSClientConstants.URL, url);

		IndexService indexService = this.indexServiceConnector.getService(theProperties);
		if (indexService == null && useProxy) {
			// throw new RuntimeException("IndexService is not available.");
			indexService = new IndexServiceProxy(theProperties);
		}
		return indexService;
	}

	/**
	 * 
	 * @param properties
	 * @param useProxy
	 * @return
	 */
	public ExtensionRegistryClient getExtensionRegistry(Map<?, ?> properties, boolean useProxy) {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(WSClientConstants.URL);
		if (url == null) {
			url = (String) properties.get(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		}

		Map<String, Object> theProperties = new HashMap<String, Object>();
		theProperties.put(WSClientConstants.REALM, realm);
		theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		theProperties.put(WSClientConstants.URL, url);

		ExtensionRegistryClient extensionRegistry = this.extensionRegistryConnector.getService(theProperties);
		if (extensionRegistry == null && useProxy) {
			// throw new RuntimeException("ExtensionRegistryClient is not available.");
			extensionRegistry = new ExtensionRegistryClientProxy(theProperties);
		}
		return extensionRegistry;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public ChannelClient getChannel(Map<?, ?> properties) {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(WSClientConstants.URL);
		if (url == null) {
			url = (String) properties.get(InfraConstants.ORBIT_CHANNEL_SERVICE_URL);
		}

		Map<String, Object> theProperties = new HashMap<String, Object>();
		theProperties.put(WSClientConstants.REALM, realm);
		theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		theProperties.put(WSClientConstants.URL, url);

		ChannelClient channelClient = this.channelsConnector.getService(theProperties);
		if (channelClient == null) {
			// throw new RuntimeException("Channels is not available.");
		}
		return channelClient;
	}

}
