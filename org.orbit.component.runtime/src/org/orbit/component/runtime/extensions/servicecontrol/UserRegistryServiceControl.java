/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier1.account.service.UserRegistryServiceImpl;
import org.orbit.platform.sdk.servicecontrol.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class UserRegistryServiceControl extends ServiceControlImpl {

	public static UserRegistryServiceControl INSTANCE = new UserRegistryServiceControl();

	protected UserRegistryServiceImpl userRegistryService;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		UserRegistryServiceImpl userRegistryService = new UserRegistryServiceImpl();
		userRegistryService.start(bundleContext);
		this.userRegistryService = userRegistryService;
	}

	@Override
	public void stop(BundleContext bundleContext, Map<String, Object> properties) {
		if (this.userRegistryService != null) {
			this.userRegistryService.stop(bundleContext);
			this.userRegistryService = null;
		}
	}

}
