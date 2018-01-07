package org.orbit.component.runtime.switcher;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;

	public static BundleContext getContext() {
		return bundleContext;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;

		OrbitSwitchers.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		OrbitSwitchers.getInstance().stop(bundleContext);

		Activator.bundleContext = null;
	}

}
