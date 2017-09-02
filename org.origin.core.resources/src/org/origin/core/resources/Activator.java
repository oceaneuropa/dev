package org.origin.core.resources;

import org.origin.core.resources.node.NodeResourceFsProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		NodeResourceFsProvider.register();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		NodeResourceFsProvider.unregister();

		Activator.context = null;
	}

}
