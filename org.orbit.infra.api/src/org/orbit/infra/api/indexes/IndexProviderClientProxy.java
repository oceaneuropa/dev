package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadata;

public class IndexProviderClientProxy implements IndexProviderClient {

	private static IndexProviderProxyImpl PROXY = new IndexProviderProxyImpl();

	protected Map<String, Object> properties;
	protected IndexProviderClient indexProvider = PROXY;

	/**
	 * 
	 * @param properties
	 */
	public IndexProviderClientProxy(Map<String, Object> properties) {
		if (properties == null) {
			throw new IllegalArgumentException("properties is null");
		}
		this.properties = properties;
	}

	protected synchronized IndexProviderClient resolve() {
		if (this.indexProvider == null || this.indexProvider.isProxy()) {
			// IndexProviderClient resolvedIndexProvider = InfraClients.getInstance().getIndexProvider0(this.properties, false);
			IndexProviderClient resolvedIndexProvider = null;
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
	public String getURL() {
		return resolve().getURL();
	}

	@Override
	public Map<String, Object> getProperties() {
		return resolve().getProperties();
	}

	@Override
	public void update(Map<String, Object> properties) {
		resolve().update(properties);
	}

	@Override
	public boolean close() throws ClientException {
		return resolve().close();
	}

	@Override
	public ServiceMetadata getMetadata() throws ClientException {
		return resolve().getMetadata();
	}

	@Override
	public String getName() throws ClientException {
		return resolve().getName();
	}

	@Override
	public boolean ping() {
		return resolve().ping();
	}

	@Override
	public String echo(String message) throws ClientException {
		return resolve().echo(message);
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		return resolve().sendRequest(request);
	}

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
	public boolean deleteIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		return resolve().deleteIndexItem(indexProviderId, indexItemId);
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

	public static class IndexProviderProxyImpl implements IndexProviderClient {
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
		public void update(Map<String, Object> properties) {
		}

		@Override
		public boolean close() throws ClientException {
			return false;
		}

		@Override
		public ServiceMetadata getMetadata() throws ClientException {
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

		@Override
		public Response sendRequest(Request request) throws ClientException {
			return null;
		}

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
		public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
			return null;
		}

		@Override
		public boolean deleteIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
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
