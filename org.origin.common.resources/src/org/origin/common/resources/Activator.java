package org.origin.common.resources;

import org.origin.common.resources.impl.misc.FolderResourceProvider;
import org.origin.common.resources.node.internal.misc.NodeResourceProvider;
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

		FolderResourceProvider.register();
		NodeResourceProvider.register();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		NodeResourceProvider.unregister();
		FolderResourceProvider.unregister();

		Activator.context = null;
	}

}
