package org.orbit.platform.api;

import org.orbit.platform.api.impl.PlatformClientCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected PlatformClientCommand platformCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		Clients.getInstance().start(bundleContext);

		this.platformCommand = new PlatformClientCommand();
		this.platformCommand.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.platformCommand != null) {
			this.platformCommand.stop(bundleContext);
			this.platformCommand = null;
		}

		Clients.getInstance().stop(bundleContext);

		Activator.context = null;
	}

}
