package org.orbit.component.connector.appstore;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.component.api.appstore.AppStore;
import org.orbit.component.api.appstore.AppStoreManager;
import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class AppStoreManagerImpl implements AppStoreManager {

	protected BundleContext bundleContext;
	protected IndexServiceLoadBalancer indexServiceLoadBalancer;

	protected ServiceRegistration<?> serviceRegistration;
	protected AppStoreIndexItemsMonitor indexItemsMonitor;

	protected AppStoreLoadBalancer appStoreLoadBalancer;
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * 
	 * @param bundleContext
	 * @param indexServiceLoadBalancer
	 */
	public AppStoreManagerImpl(BundleContext bundleContext, IndexServiceLoadBalancer indexServiceLoadBalancer) {
		this.bundleContext = bundleContext;
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
		this.indexItemsMonitor = new AppStoreIndexItemsMonitor();
		this.indexItemsMonitor.setIndexServiceLoadBalancer(this.indexServiceLoadBalancer);
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
		if (this.isStarted.compareAndSet(true, false)) {
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
	}

	@Override
	public synchronized AppStore getAppStore() {
		checkStarted();

		AppStore appStore = null;

		List<IndexItem> indexItems = this.indexItemsMonitor.getIndexItems();
		if (appStoreLoadBalancer == null) {
			for (IndexItem indexItem : indexItems) {
				Map<String, Object> props = indexItem.getProperties();

			}

		} else {

		}

		LoadBalanceService<AppStore> appStoreService = appStoreLoadBalancer.getNext();
		if (appStoreService != null) {
			appStore = appStoreService.getService();
		}
		return appStore;
	}

}
