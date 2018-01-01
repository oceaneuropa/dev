package org.spirit.world.api;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext bundleContext;
	protected static Activator instance;

	public static BundleContext getContext() {
		return bundleContext;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected WorldFactoryAdapter worldFactoryAdapter;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		this.worldFactoryAdapter = new WorldFactoryAdapter();
		this.worldFactoryAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		Activator.instance = null;
		Activator.bundleContext = null;

		if (this.worldFactoryAdapter != null) {
			this.worldFactoryAdapter.stop(bundleContext);
			this.worldFactoryAdapter = null;
		}
	}

	public WorldFactory getWorldFactory() {
		LOG.info("getWorldFactory()");

		WorldFactory factory = this.worldFactoryAdapter != null ? this.worldFactoryAdapter.getWorldFactory() : null;
		if (factory == null) {
			throw new RuntimeException("WorldFactory is not available.");
		}
		return factory;
	}

}
