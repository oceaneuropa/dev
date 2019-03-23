/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.extension.userregistry;

import java.util.Map;

import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.component.runtime.tier1.account.service.impl.UserRegistryServiceImpl;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class UserRegistryServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.UserRegistryServiceActivator";

	public static UserRegistryServiceActivator INSTANCE = new UserRegistryServiceActivator();

	@Override
	public void start(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start UserRegistryService
		UserRegistryServiceImpl userRegistryService = new UserRegistryServiceImpl(properties);
		userRegistryService.start(bundleContext);

		process.adapt(UserRegistryService.class, userRegistryService);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop UserRegistryService
		UserRegistryService userRegistry = process.getAdapter(UserRegistryService.class);
		if (userRegistry instanceof LifecycleAware) {
			((LifecycleAware) userRegistry).stop(bundleContext);
		}
	}

}
