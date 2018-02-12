package org.orbit.platform.cli;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected PlatformClientCommand platformCommand;
	protected GAIAClientCommand gaiaCommand;

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		this.platformCommand = new PlatformClientCommand();
		this.platformCommand.start(bundleContext);

		this.gaiaCommand = new GAIAClientCommand();
		this.gaiaCommand.start(bundleContext);
	}

	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		Activator.context = null;

		if (this.gaiaCommand != null) {
			this.gaiaCommand.stop(bundleContext);
			this.gaiaCommand = null;
		}

		if (this.platformCommand != null) {
			this.platformCommand.stop(bundleContext);
			this.platformCommand = null;
		}
	}

}
