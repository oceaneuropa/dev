package org.orbit.infra.api;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.channel.Channels;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClientProxy;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderProxy;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexServiceProxy;
import org.origin.common.rest.client.GlobalContext;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfraClients {

	protected static Logger LOG = LoggerFactory.getLogger(InfraClients.class);

	public static InfraClients INSTANCE = new InfraClients();

	public static InfraClients getInstance() {
		return INSTANCE;
	}

	protected ServiceConnectorAdapter<IndexProvider> indexProviderConnector;
	protected ServiceConnectorAdapter<IndexService> indexServiceConnector;
	protected ServiceConnectorAdapter<ExtensionRegistryClient> extensionRegistryConnector;
	protected ServiceConnectorAdapter<Channels> channelsConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.indexProviderConnector = new ServiceConnectorAdapter<IndexProvider>(IndexProvider.class);
		this.indexProviderConnector.start(bundleContext);

		this.indexServiceConnector = new ServiceConnectorAdapter<IndexService>(IndexService.class);
		this.indexServiceConnector.start(bundleContext);

		this.extensionRegistryConnector = new ServiceConnectorAdapter<ExtensionRegistryClient>(ExtensionRegistryClient.class);
		this.extensionRegistryConnector.start(bundleContext);

		this.channelsConnector = new ServiceConnectorAdapter<Channels>(Channels.class);
		this.channelsConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
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
	 * @return
	 */
	public IndexProvider getIndexProviderProxy(Map<?, ?> properties) {
		return new IndexProviderProxy(properties);
	}

	public IndexProvider getIndexProvider(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		}
		if (url == null) {
			throw new IllegalStateException("'" + InfraConstants.ORBIT_INDEX_SERVICE_URL + "' property is not found.");
		}
		return getIndexProvider(url);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public IndexProvider getIndexProvider(String url) {
		return getIndexProvider(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @return
	 */
	public IndexProvider getIndexProvider(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(InfraConstants.REALM, realm);
		properties.put(InfraConstants.USERNAME, username);
		properties.put(InfraConstants.URL, url);

		IndexProvider indexProvider = this.indexProviderConnector.getService(properties);
		if (indexProvider == null) {
			throw new IllegalStateException("IndexProvider is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return indexProvider;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public IndexService getIndexServiceProxy(Map<?, ?> properties) {
		return new IndexServiceProxy(properties);
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public IndexService getIndexService(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		}
		if (url == null) {
			LOG.error("'" + InfraConstants.ORBIT_INDEX_SERVICE_URL + "' property is not found.");
			throw new IllegalStateException("'" + InfraConstants.ORBIT_INDEX_SERVICE_URL + "' property is not found.");
		}
		return getIndexService(url);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public IndexService getIndexService(String url) {
		return getIndexService(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @return
	 */
	public IndexService getIndexService(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(InfraConstants.REALM, realm);
		properties.put(InfraConstants.USERNAME, username);
		properties.put(InfraConstants.URL, url);

		IndexService indexService = null;
		if (this.indexServiceConnector != null) {
			indexService = this.indexServiceConnector.getService(properties);
		}
		if (indexService == null) {
			LOG.error("IndexService is not available.");
			// throw new IllegalStateException("IndexService is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return indexService;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public ExtensionRegistryClient getExtensionRegistryProxy(Map<?, ?> properties) {
		return new ExtensionRegistryClientProxy(properties);
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public ExtensionRegistryClient getExtensionRegistry(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		}
		if (url == null) {
			LOG.error("'" + InfraConstants.ORBIT_EXTENSION_REGISTRY_URL + "' property is not found.");
			throw new IllegalStateException("'" + InfraConstants.ORBIT_EXTENSION_REGISTRY_URL + "' property is not found.");
		}
		return getExtensionRegistry(url);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public ExtensionRegistryClient getExtensionRegistry(String url) {
		return getExtensionRegistry(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @return
	 */
	public ExtensionRegistryClient getExtensionRegistry(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(InfraConstants.REALM, realm);
		properties.put(InfraConstants.USERNAME, username);
		properties.put(InfraConstants.URL, url);

		ExtensionRegistryClient extensionRegistry = null;
		if (this.extensionRegistryConnector != null) {
			extensionRegistry = this.extensionRegistryConnector.getService(properties);
		}
		if (extensionRegistry == null) {
			LOG.error("ExtensionRegistry is not available.");
			// throw new IllegalStateException("IndexService is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return extensionRegistry;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public Channels getChannels(String url) {
		return getChannels(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @return
	 */
	public Channels getChannels(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(InfraConstants.REALM, realm);
		properties.put(InfraConstants.USERNAME, username);
		properties.put(InfraConstants.URL, url);

		Channels channels = null;
		if (this.channelsConnector != null) {
			channels = this.channelsConnector.getService(properties);
		}
		if (channels == null) {
			LOG.error("Channels is not available.");
			// throw new IllegalStateException("Channels is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return channels;
	}

}

// private static Object lock = new Object[0];
// public static InfraClients getInstance() {
// if (instance == null) {
// synchronized (lock) {
// if (instance == null) {
// instance = new InfraClients();
// }
// }
// }
// return instance;
// }
