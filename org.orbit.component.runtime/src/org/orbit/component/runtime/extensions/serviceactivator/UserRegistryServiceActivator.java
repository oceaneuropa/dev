/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.extensions.serviceactivator;

import java.util.Map;

import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.component.runtime.tier1.account.service.UserRegistryServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class UserRegistryServiceActivator implements ServiceActivator {

	public static final String ID = "component.user_registry.service_activator";

	public static UserRegistryServiceActivator INSTANCE = new UserRegistryServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<String, Object> properties = context.getProperties();

		// Start UserRegistryService
		UserRegistryServiceImpl userRegistryService = new UserRegistryServiceImpl(properties);
		userRegistryService.start(bundleContext);

		process.adapt(UserRegistryService.class, userRegistryService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop UserRegistryService
		UserRegistryService userRegistry = process.getAdapter(UserRegistryService.class);
		if (userRegistry instanceof UserRegistryServiceImpl) {
			((UserRegistryServiceImpl) userRegistry).stop(bundleContext);
		}
	}

}

// @Override
// public String getName() {
// return "UserRegistry";
// }
