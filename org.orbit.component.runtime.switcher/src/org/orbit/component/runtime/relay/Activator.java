/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.relay;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;

		// Register program extensions
		Extensions.INSTANCE.start(context);

		// Start web service app relays
		OrbitRelays.getInstance().start(context);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop web service app relays
		OrbitRelays.getInstance().stop(bundleContext);

		// Unregister program extensions
		Extensions.INSTANCE.stop(bundleContext);

		Activator.context = null;
	}

}
