/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.extension.configregistry;

import java.util.Map;

import org.orbit.component.runtime.OrbitRelays;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.server.WSRelayApplication;
import org.osgi.framework.BundleContext;

public class ConfigRegistryRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.ConfigRegistryRelayActivator";

	public static ConfigRegistryRelayActivator INSTANCE = new ConfigRegistryRelayActivator();

	@Override
	public void start(IProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start relay
		WSRelayApplication relay = OrbitRelays.getInstance().createConfigRegistryRelay(bundleContext, properties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(WSRelayApplication.class, relay);
		}
	}

	@Override
	public void stop(IProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop relay
		WSRelayApplication relay = process.getAdapter(WSRelayApplication.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
