package org.origin.mgm.client.loadbalance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.api.IndexServiceConfiguration;

public class IndexProviderLoadBalancer extends LoadBalancer<IndexProvider> {

	protected static IndexProviderUnsupportedImpl UNSUPPORTED_IMPL = new IndexProviderUnsupportedImpl();

	public IndexProviderLoadBalancer(List<LoadBalanceResource<IndexProvider>> services) {
		super(services);
	}

	public IndexProvider createLoadBalancableIndexProvider() {
		return new IndexProviderLoadBalancableImpl();
	}

	public class IndexProviderLoadBalancableImpl implements IndexProvider {

		protected IndexProvider next() {
			LoadBalanceResource<IndexProvider> services = getNext();
			if (services != null) {
				return services.getService();
			}
			return UNSUPPORTED_IMPL;
		}

		@Override
		public IndexServiceConfiguration getConfiguration() {
			return next().getConfiguration();
		}

		@Override
		public int ping() {
			return next().ping();
		}

		@Override
		public List<IndexItem> getIndexItems() throws IOException {
			return next().getIndexItems();
		}

		@Override
		public List<IndexItem> getIndexItems(String indexProviderId) throws IOException {
			return next().getIndexItems(indexProviderId);
		}

		@Override
		public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException {
			return next().getIndexItems(indexProviderId, type);
		}

		@Override
		public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException {
			return next().getIndexItem(indexProviderId, type, name);
		}

		@Override
		public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			return next().getIndexItem(indexProviderId, indexItemId);
		}

		@Override
		public boolean sendCommand(String action, Map<String, Object> params) throws IOException {
			return next().sendCommand(action, params);
		}

		@Override
		public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
			return next().addIndexItem(indexProviderId, type, name, properties);
		}

		@Override
		public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			return next().removeIndexItem(indexProviderId, indexItemId);
		}

		@Override
		public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
			return next().setProperties(indexProviderId, indexItemId, properties);
		}

		@Override
		public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
			return next().setProperty(indexProviderId, indexItemId, propName, propValue, propType);
		}

		@Override
		public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException {
			return next().removeProperties(indexProviderId, indexItemId, propertyNames);
		}

		@Override
		public <T> void adapt(Class<T> clazz, T object) {
			next().adapt(clazz, object);
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return next().getAdapter(adapter);
		}
	}

	public static class IndexProviderUnsupportedImpl implements IndexProvider {

		@Override
		public IndexServiceConfiguration getConfiguration() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int ping() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<IndexItem> getIndexItems() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<IndexItem> getIndexItems(String indexProviderId) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean sendCommand(String action, Map<String, Object> params) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> void adapt(Class<T> clazz, T object) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			throw new UnsupportedOperationException();
		}

		@Override
		public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException {
			throw new UnsupportedOperationException();
		}
	}

}
