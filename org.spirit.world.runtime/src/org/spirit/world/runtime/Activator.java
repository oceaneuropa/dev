package org.spirit.world.runtime;

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

	protected WorldFactoryImpl worldFactory;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		this.worldFactory = new WorldFactoryImpl();
		this.worldFactory.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		Activator.instance = null;
		Activator.bundleContext = null;

		if (this.worldFactory != null) {
			this.worldFactory.stop(bundleContext);
			this.worldFactory = null;
		}
	}

}
