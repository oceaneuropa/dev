/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.extension.missioncontrol;

import java.util.Map;

import org.orbit.component.runtime.OrbitRelays;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.IProcess;
import org.origin.common.rest.server.WSRelayApplication;
import org.osgi.framework.BundleContext;

public class MissionControlRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.MissionControlRelayActivator";

	public static MissionControlRelayActivator INSTANCE = new MissionControlRelayActivator();

	@Override
	public void start(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start relay
		WSRelayApplication relay = OrbitRelays.getInstance().createMissionControlRelay(bundleContext, properties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(WSRelayApplication.class, relay);
		}
	}

	@Override
	public void stop(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop relay
		WSRelayApplication relay = process.getAdapter(WSRelayApplication.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
