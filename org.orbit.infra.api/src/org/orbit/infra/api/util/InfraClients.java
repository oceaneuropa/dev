package org.orbit.infra.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClientProxy;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.IndexServiceClientProxy;
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

	protected ServiceConnectorAdapter<IndexServiceClient> indexServiceConnector;
	protected ServiceConnectorAdapter<ExtensionRegistryClient> extensionRegistryConnector;
	protected ServiceConnectorAdapter<ConfigRegistryClient> configRegistryConnector;
	protected ServiceConnectorAdapter<DataCastClient> dataCastConnector;
	protected ServiceConnectorAdapter<DataTubeClient> dataTubeConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.indexServiceConnector = new ServiceConnectorAdapter<IndexServiceClient>(IndexServiceClient.class);
		this.indexServiceConnector.start(bundleContext);

		this.extensionRegistryConnector = new ServiceConnectorAdapter<ExtensionRegistryClient>(ExtensionRegistryClient.class);
		this.extensionRegistryConnector.start(bundleContext);

		this.configRegistryConnector = new ServiceConnectorAdapter<ConfigRegistryClient>(ConfigRegistryClient.class);
		this.configRegistryConnector.start(bundleContext);

		this.dataCastConnector = new ServiceConnectorAdapter<DataCastClient>(DataCastClient.class);
		this.dataCastConnector.start(bundleContext);

		this.dataTubeConnector = new ServiceConnectorAdapter<DataTubeClient>(DataTubeClient.class);
		this.dataTubeConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void stop(final BundleContext bundleContext) {
		if (this.dataCastConnector != null) {
			this.dataCastConnector.stop(bundleContext);
			this.dataCastConnector = null;
		}

		if (this.dataTubeConnector != null) {
			this.dataTubeConnector.stop(bundleContext);
			this.dataTubeConnector = null;
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
	 * @param properties
	 * @param useProxy
	 * @return
	 */
	public IndexServiceClient getIndexService(Map<?, ?> properties, boolean useProxy) {
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

		IndexServiceClient indexService = this.indexServiceConnector.getService(theProperties);
		if (indexService == null && useProxy) {
			indexService = new IndexServiceClientProxy(theProperties);
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
	 * @param configRegistryUrl
	 * @param accessToken
	 * @return
	 */
	public ConfigRegistryClient getConfigRegistryClient(String configRegistryUrl, String accessToken) {
		ConfigRegistryClient configRegistryClient = null;
		if (configRegistryUrl != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(WSClientConstants.REALM, null);
			properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
			properties.put(WSClientConstants.URL, configRegistryUrl);
			configRegistryClient = getConfigRegistryClient(properties);
		}
		return configRegistryClient;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public ConfigRegistryClient getConfigRegistryClient(Map<String, Object> properties) {
		ConfigRegistryClient configRegistryClient = this.configRegistryConnector.getService(properties);
		if (configRegistryClient == null) {
			throw new RuntimeException("ConfigRegistryClient is not available.");
		}
		return configRegistryClient;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public DataCastClient getDataCastClient(Map<String, Object> properties) {
		DataCastClient dataCastClient = this.dataCastConnector.getService(properties);
		if (dataCastClient == null) {
			throw new RuntimeException("DataCastClient is not available.");
		}
		return dataCastClient;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public DataTubeClient getDataTubeClient(Map<String, Object> properties) {
		DataTubeClient dataTubeClient = this.dataTubeConnector.getService(properties);
		if (dataTubeClient == null) {
			throw new RuntimeException("DataTubeClient is not available.");
		}
		return dataTubeClient;
	}

}

// protected ServiceConnectorAdapter<IndexProviderClient> indexProviderConnector;

// this.indexProviderConnector = new ServiceConnectorAdapter<IndexProviderClient>(IndexProviderClient.class);
// this.indexProviderConnector.start(bundleContext);

// if (this.indexProviderConnector != null) {
// this.indexProviderConnector.stop(bundleContext);
// this.indexProviderConnector = null;
// }

// /**
// *
// * @param properties
// * @param useProxy
// * @return
// */
// public IndexServiceClient getIndexService0(Map<?, ?> properties, boolean useProxy) {
// String realm = (String) properties.get(WSClientConstants.REALM);
// String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
// String url = (String) properties.get(WSClientConstants.URL);
// if (url == null) {
// url = (String) properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
// }
//
// Map<String, Object> theProperties = new HashMap<String, Object>();
// theProperties.put(WSClientConstants.REALM, realm);
// theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
// theProperties.put(WSClientConstants.URL, url);
//
// IndexServiceClient indexService = this.indexServiceConnector.getService(theProperties);
// if (indexService == null && useProxy) {
// // throw new RuntimeException("IndexService is not available.");
// indexService = new IndexServiceClientProxy(theProperties);
// }
// return indexService;
// }

// /**
// *
// * @param properties
// * @param useProxy
// * @return
// */
// public IndexProviderClient getIndexProvider0(Map<?, ?> properties, boolean useProxy) {
// String realm = (String) properties.get(WSClientConstants.REALM);
// String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
// String url = (String) properties.get(WSClientConstants.URL);
// if (url == null) {
// url = (String) properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
// }
//
// Map<String, Object> theProperties = new HashMap<String, Object>();
// theProperties.put(WSClientConstants.REALM, realm);
// theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
// theProperties.put(WSClientConstants.URL, url);
//
// // IndexProviderClient indexProvider = this.indexProviderConnector.getService(theProperties);
// IndexProviderClient indexProvider = null;
// if (indexProvider == null && useProxy) {
// // throw new RuntimeException("IndexProvider is not available.");
// indexProvider = new IndexProviderClientProxy(theProperties);
// }
// return indexProvider;
// }

// /**
// *
// * @param properties
// * @return
// */
// public DataTubeClient getDataTubeClient(Map<?, ?> properties) {
// String realm = (String) properties.get(WSClientConstants.REALM);
// String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
// String url = (String) properties.get(WSClientConstants.URL);
// if (url == null) {
// url = (String) properties.get(InfraConstants.ORBIT_CHANNEL_SERVICE_URL);
// }
//
// Map<String, Object> theProperties = new HashMap<String, Object>();
// theProperties.put(WSClientConstants.REALM, realm);
// theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
// theProperties.put(WSClientConstants.URL, url);
//
// DataTubeClient dataTubeClient = this.dataTubeConnector.getService(theProperties);
// if (dataTubeClient == null) {
// // throw new RuntimeException("Channels is not available.");
// }
// return dataTubeClient;
// }
