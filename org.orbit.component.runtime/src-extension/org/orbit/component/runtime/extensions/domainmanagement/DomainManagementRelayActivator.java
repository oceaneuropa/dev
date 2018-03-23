/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.extensions.domainmanagement;

import java.util.Map;

import org.orbit.component.runtime.relay.OrbitRelays;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.origin.common.rest.server.WSRelayApplication;
import org.osgi.framework.BundleContext;

public class DomainManagementRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.DomainManagementRelayActivator";

	public static DomainManagementRelayActivator INSTANCE = new DomainManagementRelayActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start relay
		WSRelayApplication relay = OrbitRelays.getInstance().createDomainManagementRelay(bundleContext, properties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(WSRelayApplication.class, relay);
		}
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop relay
		WSRelayApplication relay = process.getAdapter(WSRelayApplication.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
