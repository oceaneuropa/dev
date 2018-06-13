package org.orbit.infra.api;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.channel.Channels;
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

	// private static Object lock = new Object[0];
	public static InfraClients INSTANCE = new InfraClients();

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
	public static InfraClients getInstance() {
		return INSTANCE;
	}

	protected ServiceConnectorAdapter<IndexProvider> indexProviderConnector;
	protected ServiceConnectorAdapter<IndexService> indexServiceConnector;
	protected ServiceConnectorAdapter<Channels> channelsConnector;

	public void start(final BundleContext bundleContext) {
		this.indexProviderConnector = new ServiceConnectorAdapter<IndexProvider>(IndexProvider.class);
		this.indexProviderConnector.start(bundleContext);

		this.indexServiceConnector = new ServiceConnectorAdapter<IndexService>(IndexService.class);
		this.indexServiceConnector.start(bundleContext);

		this.channelsConnector = new ServiceConnectorAdapter<Channels>(Channels.class);
		this.channelsConnector.start(bundleContext);
	}

	public void stop(final BundleContext bundleContext) {
		if (this.channelsConnector != null) {
			this.channelsConnector.stop(bundleContext);
			this.channelsConnector = null;
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

	public IndexProvider getIndexProvider(String url) {
		return getIndexProvider(null, null, url);
	}

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

	public IndexService getIndexServiceProxy(Map<?, ?> properties) {
		return new IndexServiceProxy(properties);
	}

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

	public IndexService getIndexService(String url) {
		return getIndexService(null, null, url);
	}

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

	public Channels getChannels(String url) {
		return getChannels(null, null, url);
	}

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
