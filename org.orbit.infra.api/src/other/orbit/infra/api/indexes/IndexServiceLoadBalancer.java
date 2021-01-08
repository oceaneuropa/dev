package other.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemUpdater;
import org.orbit.infra.api.indexes.IndexProviderItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadata;

public class IndexServiceLoadBalancer extends LoadBalancer<IndexServiceClient> {

	protected static IndexServiceUnsupportedImpl UNSUPPORTED_IMPL = new IndexServiceUnsupportedImpl();

	/**
	 * 
	 * @param services
	 */
	public IndexServiceLoadBalancer(List<LoadBalanceResource<IndexServiceClient>> services) {
		super(services);
	}

	public IndexServiceClient createLoadBalancableIndexService() {
		return new IndexServiceLoadBalancableImpl();
	}

	protected IndexServiceClient next() {
		LoadBalanceResource<IndexServiceClient> lbServices = this.getNext();
		if (lbServices != null) {
			return lbServices.getService();
		}
		return UNSUPPORTED_IMPL;
	}

	public class IndexServiceLoadBalancableImpl implements IndexServiceClient {
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
		public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			return next().removeIndexItem(indexProviderId, indexItemId);
		}

		@Override
		public boolean removeIndexItems(String indexProviderId) throws IOException {
			return next().removeIndexItems(indexProviderId);
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
		public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException {
			// TODO Auto-generated method stub
			return false;
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

		@Override
		public List<IndexProviderItem> getIndexProviders() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IndexProviderItem addIndexProvider(String id, String name, String description) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean deleteIndexProvider(String id) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}
	}

	public static class IndexServiceUnsupportedImpl implements IndexServiceClient {
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
		public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeIndexItems(String indexProviderId) throws IOException {
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
		public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException {
			// TODO Auto-generated method stub
			return false;
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

		@Override
		public List<IndexProviderItem> getIndexProviders() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IndexProviderItem addIndexProvider(String id, String name, String description) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean deleteIndexProvider(String id) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}
	}

}

// public static final String INDEX_SERVICE_INDEXER_ID = "component_.index_service.indexer";
// protected ThreadPoolTimer indexServicesMonitor;

// @Override
// public void update(Map<Object, Object> properties) {
// throw new UnsupportedOperationException();
// }

// @Override
// public void update(Map<Object, Object> properties) {
// getNextIndexService().update(properties);
// }

// @Override
// public void start() {
// super.start();
//
// // Start checking the index items for index services themselves using the IndexService API.
// // e.g. a remote IndexService is added to the indexing
// // e.g. a remote IndexService is removed from the indexing
// // For the refreshing of the properties of existing index items, the corresponding LoadBalanceService will do that by calling its referenced
// IndexService.
// if (this.indexServicesMonitor != null) {
// this.indexServicesMonitor.stop();
// }
// this.indexServicesMonitor = new ThreadPoolTimer("IndexServiceLoadBalancer(IndexService) Monitor", new Runnable() {
// @Override
// public void run() {
// monitor();
// }
// });
// this.indexServicesMonitor.start();
// }

// @Override
// public void stop() {
// if (this.indexServicesMonitor != null) {
// this.indexServicesMonitor.stop();
// this.indexServicesMonitor = null;
// }
//
// super.stop();
// }

// protected void monitor() {
// // LoadBalanceService<IndexService> lbService = getFirstAvailableService();
// LoadBalanceService<IndexService> lbService = null;
// if (lbService == null) {
// return;
// }
// try {
// List<IndexItem> indexItems = lbService.getService().getIndexItems(INDEX_SERVICE_INDEXER_ID);
//
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// protected LoadBalanceService<IndexService> getFirstAvailableService() {
// if (this.services.isEmpty()) {
// return null;
// } else if (this.services.size() == 1) {
// return this.services.get(0);
// }
//
// int latestPingIndex = -1;
// Date latestPingTime = null;
// for (int i = 0; i < this.services.size(); i++) {
// LoadBalanceService<IndexService> lbService = this.services.get(i);
// if (lbService.hasProperty("ping") && lbService.hasProperty("last_ping_time")) {
// int ping = (int) lbService.getProperty("ping");
// if (ping > 0) {
// Date currPingTime = (Date) lbService.getProperty("last_ping_time");
// if (latestPingTime == null || currPingTime.after(latestPingTime)) {
// latestPingTime = currPingTime;
// latestPingIndex = i;
// }
// }
// }
// }
// if (latestPingIndex >= 0) {
// return this.services.get(latestPingIndex);
// }
//
// // none of the services have valid ping result.
// return this.services.get(0);
// }
