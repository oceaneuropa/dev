package org.origin.core.workspace;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ResourcesPlugin implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ResourcesPlugin.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		ResourcesPlugin.context = null;
	}

}
