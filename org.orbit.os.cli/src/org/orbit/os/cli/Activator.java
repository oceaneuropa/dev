package org.orbit.os.cli;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected GAIACommand gaiaCommand;

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		this.gaiaCommand = new GAIACommand();
		this.gaiaCommand.start(bundleContext);
	}

	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		Activator.context = null;

		if (this.gaiaCommand != null) {
			this.gaiaCommand.stop(bundleContext);
			this.gaiaCommand = null;
		}
	}

}
