package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;

public class IndexServiceLoadBalancer extends LoadBalancer<IndexService> {

	protected static IndexServiceUnsupportedImpl UNSUPPORTED_IMPL = new IndexServiceUnsupportedImpl();

	/**
	 * 
	 * @param services
	 */
	public IndexServiceLoadBalancer(List<LoadBalanceResource<IndexService>> services) {
		super(services);
	}

	public IndexService createLoadBalancableIndexService() {
		return new IndexServiceLoadBalancableImpl();
	}

	protected IndexService next() {
		LoadBalanceResource<IndexService> lbServices = this.getNext();
		if (lbServices != null) {
			return lbServices.getService();
		}
		return UNSUPPORTED_IMPL;
	}

	public class IndexServiceLoadBalancableImpl implements IndexService {
		@Override
		public String getName() {
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

		// @Override
		// public void update(Map<Object, Object> properties) {
		// getNextIndexService().update(properties);
		// }

		@Override
		public boolean ping() {
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
		public <T> void adapt(Class<T> clazz, T object) {
			next().adapt(clazz, object);
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return next().getAdapter(adapter);
		}

		@Override
		public boolean isProxy() {
			return false;
		}
	}

	public static class IndexServiceUnsupportedImpl implements IndexService {
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

		// @Override
		// public void update(Map<Object, Object> properties) {
		// throw new UnsupportedOperationException();
		// }

		@Override
		public boolean ping() {
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
		public boolean isProxy() {
			return false;
		}
	}

}

// public static final String INDEX_SERVICE_INDEXER_ID = "component_.index_service.indexer";
// protected ThreadPoolTimer indexServicesMonitor;

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
