package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraClients;

public class IndexServiceProxy implements IndexService {

	private static IndexServiceProxyImpl PROXY = new IndexServiceProxyImpl();

	protected Map<?, ?> properties;
	protected String realm;
	protected String username;
	protected String url;

	protected IndexService indexService = PROXY;

	public IndexServiceProxy(Map<?, ?> properties) {
		this.properties = properties;
	}

	public IndexServiceProxy(String url) {
		this(null, null, url);
	}

	public IndexServiceProxy(String realm, String username, String url) {
		this.realm = realm;
		this.username = username;
		this.url = url;
	}

	protected synchronized IndexService resolve() {
		if (this.indexService == null || this.indexService.isProxy()) {
			IndexService resolvedIndexService = null;
			if (this.properties != null) {
				resolvedIndexService = InfraClients.getInstance().getIndexService(this.properties);
			}
			if (resolvedIndexService == null) {
				if (this.url != null) {
					resolvedIndexService = InfraClients.getInstance().getIndexService(this.realm, this.username, this.url);
				}
			}
			if (resolvedIndexService != null && !resolvedIndexService.isProxy()) {
				this.indexService = resolvedIndexService;
			}
		}
		if (this.indexService == null) {
			this.indexService = PROXY;
		}
		return this.indexService;
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

	public static class IndexServiceProxyImpl implements IndexService {

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
		public boolean isProxy() {
			return true;
		}
	}

}
