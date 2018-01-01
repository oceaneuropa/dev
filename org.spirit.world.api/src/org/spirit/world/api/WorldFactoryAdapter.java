package org.spirit.world.api;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldFactoryAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(WorldFactoryAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<WorldFactory, WorldFactory> serviceTracker;

	public WorldFactoryAdapter() {
	}

	public WorldFactory getWorldFactory() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking WorldFactory service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<WorldFactory, WorldFactory>(bundleContext, WorldFactory.class, new ServiceTrackerCustomizer<WorldFactory, WorldFactory>() {
			@Override
			public WorldFactory addingService(ServiceReference<WorldFactory> reference) {
				WorldFactory factory = bundleContext.getService(reference);
				LOG.info("addingService() WorldFactory is added: " + factory);
				factoryAdded(factory);
				return factory;
			}

			@Override
			public void modifiedService(ServiceReference<WorldFactory> reference, WorldFactory factory) {
				LOG.info("removedService() WorldFactory is modified: " + factory);
			}

			@Override
			public void removedService(ServiceReference<WorldFactory> reference, WorldFactory factory) {
				LOG.info("removedService() WorldFactory is removed: " + factory);
				factoryRemoved(factory);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking WorldFactory service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void factoryAdded(WorldFactory factory) {

	}

	public void factoryRemoved(WorldFactory factory) {

	}

}
