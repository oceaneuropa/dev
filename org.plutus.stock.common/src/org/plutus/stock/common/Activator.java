package org.plutus.stock.common;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.plutus.stock.resource.StockDataResourceFactory;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		StockDataResourceFactory.register();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		StockDataResourceFactory.unregister();
	}

}
