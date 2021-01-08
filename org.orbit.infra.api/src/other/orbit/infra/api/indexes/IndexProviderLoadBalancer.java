package other.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemUpdater;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.orbit.infra.api.indexes.IndexProviderItem;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadata;

public class IndexProviderLoadBalancer extends LoadBalancer<IndexProviderClient> {

	protected static IndexProviderUnsupportedImpl UNSUPPORTED_IMPL = new IndexProviderUnsupportedImpl();

	public IndexProviderLoadBalancer(List<LoadBalanceResource<IndexProviderClient>> services) {
		super(services);
	}

	public IndexProviderClient createLoadBalancableIndexProvider() {
		return new IndexProviderLoadBalancableImpl();
	}

	public class IndexProviderLoadBalancableImpl implements IndexProviderClient {

		protected IndexProviderClient next() {
			LoadBalanceResource<IndexProviderClient> services = getNext();
			if (services != null) {
				return services.getService();
			}
			return UNSUPPORTED_IMPL;
		}

		@Override
		public String getName() throws ClientException {
			return next().getName();
		}

		@Override
		public String getURL() {
			return next().getURL();
		}

		@Override
		public Map<String, Object> getProperties() {
			return next().getProperties();
		}

		@Override
		public boolean ping() {
			return next().ping();
		}

		@Override
		public String echo(String message) throws ClientException {
			return next().echo(message);
		}

		@Override
		public List<IndexProviderItem> getIndexProviders() throws IOException {
			return next().getIndexProviders();
		}

		@Override
		public IndexProviderItem addIndexProvider(String id, String name, String description) throws IOException {
			return next().addIndexProvider(id, name, description);
		}

		@Override
		public boolean deleteIndexProvider(String id) throws IOException {
			return next().deleteIndexProvider(id);
		}

		// @Override
		// public List<IndexItem> getIndexItems() throws IOException {
		// return next().getIndexItems();
		// }

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
		public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
			return next().addIndexItem(indexProviderId, type, name, properties);
		}

		@Override
		public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			return next().removeIndexItem(indexProviderId, indexItemId);
		}

		@Override
		public boolean removeIndexItems(String indexProviderId) throws IOException {
			return next().removeIndexItems(indexProviderId);
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
		public <T> void adapt(Class<T>[] classes, T object) {
			next().adapt(classes, object);
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return next().getAdapter(adapter);
		}

		@Override
		public <T> Class<T>[] getAdaptedClasses() {
			return next().getAdaptedClasses();
		}

		@Override
		public boolean isProxy() {
			return false;
		}

		@Override
		public void update(Map<String, Object> properties) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean close() throws ClientException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public ServiceMetadata getMetadata() throws ClientException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Response sendRequest(Request request) throws ClientException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IndexItemUpdater> getIndexItemUpdaters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addIndexItemUpdater(IndexItemUpdater updater) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeIndexItemUpdater(IndexItemUpdater updater) {
			// TODO Auto-generated method stub

		}
	}

	public static class IndexProviderUnsupportedImpl implements IndexProviderClient {
		@Override
		public String getName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getURL() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, Object> getProperties() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean ping() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String echo(String message) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<IndexProviderItem> getIndexProviders() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public IndexProviderItem addIndexProvider(String id, String name, String description) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean deleteIndexProvider(String id) throws IOException {
			throw new UnsupportedOperationException();
		}

		// @Override
		// public List<IndexItem> getIndexItems() throws IOException {
		// throw new UnsupportedOperationException();
		// }

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
		public <T> void adapt(Class<T> clazz, T object) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> void adapt(Class<T>[] classes, T object) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> Class<T>[] getAdaptedClasses() {
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
		public boolean removeIndexItems(String indexProviderId) throws IOException {
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

		@Override
		public boolean isProxy() {
			return false;
		}

		@Override
		public void update(Map<String, Object> properties) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean close() throws ClientException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public ServiceMetadata getMetadata() throws ClientException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Response sendRequest(Request request) throws ClientException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IndexItemUpdater> getIndexItemUpdaters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addIndexItemUpdater(IndexItemUpdater updater) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeIndexItemUpdater(IndexItemUpdater updater) {
			// TODO Auto-generated method stub

		}
	}

}
