package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraClients;

public class IndexProviderProxy implements IndexProvider {

	private static IndexProviderProxyImpl PROXY = new IndexProviderProxyImpl();

	protected Map<?, ?> properties;
	protected String realm;
	protected String username;
	protected String url;

	protected IndexProvider indexProvider = PROXY;

	public IndexProviderProxy(Map<?, ?> properties) {
		this.properties = properties;
	}

	public IndexProviderProxy(String url) {
		this(null, null, url);
	}

	public IndexProviderProxy(String realm, String username, String url) {
		this.realm = realm;
		this.username = username;
		this.url = url;
	}

	protected synchronized IndexProvider resolve() {
		if (this.indexProvider == null || this.indexProvider.isProxy()) {
			IndexProvider resolvedIndexProvider = null;
			if (this.properties != null) {
				resolvedIndexProvider = InfraClients.getInstance().getIndexProvider(this.properties);
			}
			if (resolvedIndexProvider == null) {
				if (this.url != null) {
					resolvedIndexProvider = InfraClients.getInstance().getIndexProvider(this.realm, this.username, this.url);
				}
			}
			if (resolvedIndexProvider != null && !resolvedIndexProvider.isProxy()) {
				this.indexProvider = resolvedIndexProvider;
			}
		}
		if (this.indexProvider == null) {
			this.indexProvider = PROXY;
		}
		return this.indexProvider;
	}

	@Override
	public String getName() {
		return resolve().getName();
	}

	@Override
	public String getURL() {
		return resolve().getURL();
	}

	@Override
	public Map<String, Object> getProperties() {
		return resolve().getProperties();
	}

	@Override
	public boolean ping() {
		return resolve().ping();
	}

	@Override
	public String echo(String message) throws IOException {
		return resolve().echo(message);
	}

	@Override
	public boolean sendCommand(String action, Map<String, Object> params) throws IOException {
		return resolve().sendCommand(action, params);
	}

	// @Override
	// public List<IndexItem> getIndexItems() throws IOException {
	// return resolve().getIndexItems();
	// }

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId) throws IOException {
		return resolve().getIndexItems(indexProviderId);
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException {
		return resolve().getIndexItems(indexProviderId, type);
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException {
		return resolve().getIndexItem(indexProviderId, type, name);
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		return resolve().getIndexItem(indexProviderId, indexItemId);
	}

	@Override
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
		return resolve().addIndexItem(indexProviderId, type, name, properties);
	}

	@Override
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		return resolve().removeIndexItem(indexProviderId, indexItemId);
	}

	@Override
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
		return resolve().setProperties(indexProviderId, indexItemId, properties);
	}

	@Override
	public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
		return resolve().setProperty(indexProviderId, indexItemId, propName, propValue, propType);
	}

	@Override
	public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException {
		return resolve().removeProperties(indexProviderId, indexItemId, propertyNames);
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		resolve().adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		resolve().adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return resolve().getAdapter(adapter);
	}

	@Override
	public boolean isProxy() {
		return false;
	}

	public static class IndexProviderProxyImpl implements IndexProvider {

		@Override
		public String getName() {
			return null;
		}

		@Override
		public String getURL() {
			return null;
		}

		@Override
		public Map<String, Object> getProperties() {
			return null;
		}

		@Override
		public boolean ping() {
			return false;
		}

		@Override
		public String echo(String message) {
			return null;
		}

		// @Override
		// public List<IndexItem> getIndexItems() throws IOException {
		// return null;
		// }

		@Override
		public List<IndexItem> getIndexItems(String indexProviderId) throws IOException {
			return null;
		}

		@Override
		public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException {
			return null;
		}

		@Override
		public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException {
			return null;
		}

		@Override
		public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			return null;
		}

		@Override
		public boolean sendCommand(String action, Map<String, Object> params) throws IOException {
			return false;
		}

		@Override
		public <T> void adapt(Class<T> clazz, T object) {
		}

		@Override
		public <T> void adapt(Class<T>[] classes, T object) {
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return null;
		}

		@Override
		public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
			return null;
		}

		@Override
		public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			return false;
		}

		@Override
		public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
			return false;
		}

		@Override
		public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
			return false;
		}

		@Override
		public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException {
			return false;
		}

		@Override
		public boolean isProxy() {
			return true;
		}
	}

}
