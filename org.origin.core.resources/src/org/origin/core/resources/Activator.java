package org.origin.core.resources;

import org.origin.core.resources.node.extension.NodeResourceProvider;
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

		NodeResourceProvider.register();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		NodeResourceProvider.unregister();

		Activator.context = null;
	}

}
