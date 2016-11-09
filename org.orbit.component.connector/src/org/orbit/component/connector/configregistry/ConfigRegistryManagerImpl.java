package org.orbit.component.connector.configregistry;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.component.api.configregistry.ConfigRegistryManager;
import org.orbit.component.api.configregistry.ConfigRegistry;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ConfigRegistryManagerImpl implements ConfigRegistryManager {

	protected BundleContext bundleContext;
	protected IndexServiceLoadBalancer indexServiceLoadBalancer;

	protected ServiceRegistration<?> serviceRegistration;
	protected ConfigRegistryIndexItemsMonitor indexItemsMonitor;

	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * 
	 * @param bundleContext
	 * @param indexServiceLoadBalancer
	 */
	public ConfigRegistryManagerImpl(BundleContext bundleContext, IndexServiceLoadBalancer indexServiceLoadBalancer) {
		this.bundleContext = bundleContext;
		this.indexServiceLoadBalancer = indexServiceLoadBalancer;
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException("ConfigRegistryManager is not started.");
		}
	}

	/**
	 * Start the ConfigRegistry manager.
	 * 
	 * 1. Start a monitor to sync ConfigRegistry index items
	 * 
	 * 2. Register ConfigRegistryManager service.
	 * 
	 */
	public synchronized void start() {
		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		// -----------------------------------------------------------------------------
		// Start monitor
		// -----------------------------------------------------------------------------
		// Start monitor to periodically get ConfigRegistry index items
		this.indexItemsMonitor = new ConfigRegistryIndexItemsMonitor();
		this.indexItemsMonitor.setIndexServiceLoadBalancer(this.indexServiceLoadBalancer);
		this.indexItemsMonitor.start();

		// -----------------------------------------------------------------------------
		// Register service
		// -----------------------------------------------------------------------------
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = this.bundleContext.registerService(ConfigRegistryManager.class, this, props);
	}

	/**
	 * Stop the ConfigRegistry manager.
	 * 
	 * 1. Unregister ConfigRegistryManager service.
	 * 
	 * 2. Stop the monitor to sync ConfigRegistry index items.
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
	public ConfigRegistry getConfigRegistryService() {
		checkStarted();
		return null;
	}

}
