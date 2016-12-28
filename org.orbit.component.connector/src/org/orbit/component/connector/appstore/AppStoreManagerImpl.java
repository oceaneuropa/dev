package org.orbit.component.connector.appstore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.orbit.component.api.appstore.AppStore;
import org.orbit.component.api.appstore.AppStoreManager;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemsMonitor;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class AppStoreManagerImpl implements AppStoreManager {

	protected BundleContext bundleContext;
	protected IndexServiceLoadBalancer indexServiceLoadBalancer;

	protected ServiceRegistration<?> serviceRegistration;
	protected IndexItemsMonitor indexItemsMonitor;

	protected AppStoreLoadBalancer appStoreLoadBalancer;
	protected ReadWriteLock appStoreRWLock;
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	public AppStoreManagerImpl() {
		this.appStoreRWLock = new ReentrantReadWriteLock();
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public IndexServiceLoadBalancer getIndexServiceLoadBalancer() {
		return indexServiceLoadBalancer;
	}

	public void setIndexServiceLoadBalancer(IndexServiceLoadBalancer indexServiceLoadBalancer) {
		this.indexServiceLoadBalancer = indexServiceLoadBalancer;
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException("AppStoreManager is not started.");
		}
	}

	/**
	 * Start the AppStore manager.
	 * 
	 * 1. Start a monitor to sync AppStore index items
	 * 
	 * 2. Register AppStoreManager service.
	 * 
	 */
	public synchronized void start() {
		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		// -----------------------------------------------------------------------------
		// Start Monitor
		// -----------------------------------------------------------------------------
		// Start monitor to periodically get AppStores index items
		this.indexItemsMonitor = new IndexItemsMonitor("AppStore Index Items Monitor", this.indexServiceLoadBalancer) {
			@Override
			protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
				return indexService.getIndexItems(OrbitConstants.APP_STORE_INDEXER_ID, OrbitConstants.APP_STORE_TYPE);
			}

			@Override
			protected void indexItemsUpdated(List<IndexItem> indexItems) {
				updateLoadBalancer(indexItems);
			}
		};
		this.indexItemsMonitor.start();

		// -----------------------------------------------------------------------------
		// Register service
		// -----------------------------------------------------------------------------
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = this.bundleContext.registerService(AppStoreManager.class, this, props);
	}

	/**
	 * Stop the AppStore manager.
	 * 
	 * 1. Unregister AppStoreManager service.
	 * 
	 * 2. Stop the monitor to sync AppStore index items.
	 * 
	 */
	public synchronized void stop() {
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// -----------------------------------------------------------------------------
		// Unregister service
		// -----------------------------------------------------------------------------
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		// -----------------------------------------------------------------------------
		// Stop monitor
		// -----------------------------------------------------------------------------
		if (this.indexItemsMonitor != null) {
			this.indexItemsMonitor.stop();
			this.indexItemsMonitor = null;
		}
	}

	@Override
	public synchronized AppStore getAppStore() {
		checkStarted();

		if (this.appStoreLoadBalancer == null) {
			List<IndexItem> appStoreIndexItems = this.indexItemsMonitor.getCachedIndexItems();
			updateLoadBalancer(appStoreIndexItems);
		}

		AppStore appStore = null;
		try {
			this.appStoreRWLock.readLock().lock();
			LoadBalanceService<AppStore> appStoreService = this.appStoreLoadBalancer.getNext();
			if (appStoreService != null) {
				appStore = appStoreService.getService();
			}
		} finally {
			this.appStoreRWLock.readLock().unlock();
		}
		return appStore;
	}

	/**
	 * Create or update AppStoreLoadBalancer with latest app store index items.
	 * 
	 * @param appStoreIndexItems
	 */
	protected synchronized void updateLoadBalancer(List<IndexItem> appStoreIndexItems) {
		this.appStoreRWLock.writeLock().lock();

		try {
			Map<Integer, IndexItem> indexItemMap = new HashMap<Integer, IndexItem>();

			for (IndexItem appStoreIndexItem : appStoreIndexItems) {
				Integer indexItemId = appStoreIndexItem.getIndexItemId();
				indexItemMap.put(indexItemId, appStoreIndexItem);
			}

			if (this.appStoreLoadBalancer == null) {
				List<LoadBalanceService<AppStore>> services = new ArrayList<LoadBalanceService<AppStore>>();
				for (IndexItem appStoreIndexItem : appStoreIndexItems) {
					Integer indexItemId = appStoreIndexItem.getIndexItemId();
					String appStoreName = appStoreIndexItem.getName();
					Map<String, Object> props = appStoreIndexItem.getProperties();
					String hostURL = (String) props.get(OrbitConstants.INDEX_ITEM_URL_PROP);
					String contextRoot = (String) props.get(OrbitConstants.INDEX_ITEM_CONTEXT_ROOT_PROP);
					Date lastHeartBeatTime = (Date) props.get(OrbitConstants.INDEX_ITEM_LAST_HEARTBEAT_TIME_PROP);

					Map<String, Object> appStoreProperties = new HashMap<String, Object>();
					appStoreProperties.put(OrbitConstants.INDEX_ITEM_ID_PROP, indexItemId);
					appStoreProperties.put(OrbitConstants.APPSTORE_NAME, appStoreName);
					appStoreProperties.put(OrbitConstants.APPSTORE_URL, hostURL);
					appStoreProperties.put(OrbitConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
					appStoreProperties.put(OrbitConstants.APPSTORE_LAST_HEARTBEAT_TIME, lastHeartBeatTime);

					AppStore newAppStore = new AppStoreImpl(appStoreProperties);
					AppStoreLoadBalanceService service = new AppStoreLoadBalanceService(newAppStore, appStoreProperties);
					service.setProperties(appStoreProperties);

					services.add(service);
				}

				this.appStoreLoadBalancer = new AppStoreLoadBalancer(services);

			} else {
				List<LoadBalanceService<AppStore>> servicesToRemove = new ArrayList<LoadBalanceService<AppStore>>();

				List<LoadBalanceService<AppStore>> services = this.appStoreLoadBalancer.getServices();
				for (LoadBalanceService<AppStore> service : services) {
					Integer currIndexItemId = (Integer) service.getProperty(OrbitConstants.INDEX_ITEM_ID_PROP);
					if (currIndexItemId == null) {
						throw new RuntimeException("AppStore LoadBalanceService's index_item_id property is not available.");
					}

					if (indexItemMap.containsKey(currIndexItemId)) {
						IndexItem appStoreIndexItem = indexItemMap.get(currIndexItemId);

						Integer indexItemId = appStoreIndexItem.getIndexItemId();
						String appStoreName = appStoreIndexItem.getName();
						Map<String, Object> newProps = appStoreIndexItem.getProperties();
						String hostURL = (String) newProps.get(OrbitConstants.INDEX_ITEM_URL_PROP);
						String contextRoot = (String) newProps.get(OrbitConstants.INDEX_ITEM_CONTEXT_ROOT_PROP);
						Date lastHeartBeatTime = (Date) newProps.get(OrbitConstants.INDEX_ITEM_LAST_HEARTBEAT_TIME_PROP);

						Map<String, Object> newAppStoreProperties = new HashMap<String, Object>();
						newAppStoreProperties.put(OrbitConstants.INDEX_ITEM_ID_PROP, indexItemId);
						newAppStoreProperties.put(OrbitConstants.APPSTORE_NAME, appStoreName);
						newAppStoreProperties.put(OrbitConstants.APPSTORE_URL, hostURL);
						newAppStoreProperties.put(OrbitConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
						newAppStoreProperties.put(OrbitConstants.APPSTORE_LAST_HEARTBEAT_TIME, lastHeartBeatTime);

					} else {
						// no latest index item for current service.
						servicesToRemove.add(service);
					}
				}

				if (!servicesToRemove.isEmpty()) {
					for (LoadBalanceService<AppStore> serviceToRemove : servicesToRemove) {
						this.appStoreLoadBalancer.removeService(serviceToRemove);
					}
				}
			}

		} finally {
			this.appStoreRWLock.writeLock().unlock();
		}
	}

}
