/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.extension.identity;

import java.util.Map;

import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.component.runtime.tier1.identity.service.IdentityServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class IdentityServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.IdentityServiceActivator";

	public static IdentityServiceActivator INSTANCE = new IdentityServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start IdentityService
		IdentityServiceImpl identityService = new IdentityServiceImpl(properties);
		identityService.start(bundleContext);

		process.adapt(IdentityService.class, identityService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop IdentityService
		IdentityService identityService = process.getAdapter(IdentityService.class);
		if (identityService instanceof LifecycleAware) {
			((LifecycleAware) identityService).stop(bundleContext);
		}
	}

}
