package org.orbit.infra.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.subscription.SubsServerAPI;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.service.ILifecycle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class InfraClients implements ILifecycle {

	protected static Logger LOG = LoggerFactory.getLogger(InfraClients.class);

	public static InfraClients INSTANCE = new InfraClients();

	public static InfraClients getInstance() {
		return INSTANCE;
	}

	protected ServiceConnectorAdapter<IndexServiceClient> indexServiceConnector;
	protected ServiceConnectorAdapter<ExtensionRegistryClient> extensionRegistryConnector;
	protected ServiceConnectorAdapter<ConfigRegistryClient> configRegistryConnector;
	protected ServiceConnectorAdapter<SubsServerAPI> subsServerConnector;

	/** ILifecycle */
	@Override
	public void start(final BundleContext bundleContext) {
		this.indexServiceConnector = new ServiceConnectorAdapter<IndexServiceClient>(IndexServiceClient.class);
		this.indexServiceConnector.start(bundleContext);

		this.extensionRegistryConnector = new ServiceConnectorAdapter<ExtensionRegistryClient>(ExtensionRegistryClient.class);
		this.extensionRegistryConnector.start(bundleContext);

		this.configRegistryConnector = new ServiceConnectorAdapter<ConfigRegistryClient>(ConfigRegistryClient.class);
		this.configRegistryConnector.start(bundleContext);

		this.subsServerConnector = new ServiceConnectorAdapter<SubsServerAPI>(SubsServerAPI.class);
		this.subsServerConnector.start(bundleContext);
	}

	@Override
	public void stop(final BundleContext bundleContext) {
		if (this.subsServerConnector != null) {
			this.subsServerConnector.stop(bundleContext);
			this.subsServerConnector = null;
		}

		if (this.configRegistryConnector != null) {
			this.configRegistryConnector.stop(bundleContext);
			this.configRegistryConnector = null;
		}

		if (this.extensionRegistryConnector != null) {
			this.extensionRegistryConnector.stop(bundleContext);
			this.extensionRegistryConnector = null;
		}

		if (this.indexServiceConnector != null) {
			this.indexServiceConnector.stop(bundleContext);
			this.indexServiceConnector = null;
		}
	}

	/**
	 * 
	 * @param url
	 * @param accessToken
	 * @return
	 */
	public IndexServiceClient getIndexService(String url, String accessToken) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSClientConstants.REALM, null);
		properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		properties.put(WSClientConstants.URL, url);

		IndexServiceClient client = this.indexServiceConnector.getService(properties);
		return client;
	}

	/**
	 * 
	 * @param url
	 * @param accessToken
	 * @return
	 */
	public ExtensionRegistryClient getExtensionRegistryClient(String url, String accessToken) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSClientConstants.REALM, null);
		properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		properties.put(WSClientConstants.URL, url);

		ExtensionRegistryClient client = this.extensionRegistryConnector.getService(properties);
		return client;
	}

	/**
	 * 
	 * @param url
	 * @param accessToken
	 * @return
	 */
	public ConfigRegistryClient getConfigRegistryClient(String url, String accessToken) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSClientConstants.REALM, null);
		properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		properties.put(WSClientConstants.URL, url);

		ConfigRegistryClient client = this.configRegistryConnector.getService(properties);
		return client;
	}

	/**
	 * 
	 * @param url
	 * @param accessToken
	 * @return
	 */
	public SubsServerAPI getSubsServerAPI(String url, String accessToken) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSClientConstants.REALM, null);
		properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		properties.put(WSClientConstants.URL, url);

		SubsServerAPI api = this.subsServerConnector.getService(properties);
		return api;
	}

}
