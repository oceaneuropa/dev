package org.orbit.spirit.api;

import org.orbit.spirit.api.gaia.GAIAClientCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected GAIAClientCommand gaiaCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		Clients.getInstance().start(bundleContext);

		this.gaiaCommand = new GAIAClientCommand();
		this.gaiaCommand.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.gaiaCommand != null) {
			this.gaiaCommand.stop(bundleContext);
			this.gaiaCommand = null;
		}

		Clients.getInstance().stop(bundleContext);

		Activator.context = null;
	}

}
