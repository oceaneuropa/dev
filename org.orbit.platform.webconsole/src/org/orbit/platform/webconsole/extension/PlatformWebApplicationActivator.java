/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.webconsole.extension;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.platform.webconsole.servlet.PlatformWebApplication;
import org.osgi.framework.BundleContext;

public class PlatformWebApplicationActivator implements ServiceActivator {

	public static final String ID = "org.orbit.platform.webconsole.WebApplicationActivator";

	public static PlatformWebApplicationActivator INSTANCE = new PlatformWebApplicationActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		PlatformWebApplication webApp = new PlatformWebApplication(properties);
		webApp.start(bundleContext);

		process.adapt(PlatformWebApplication.class, webApp);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		PlatformWebApplication webApp = process.getAdapter(PlatformWebApplication.class);
		if (webApp != null) {
			webApp.stop(bundleContext);
		}
	}

}
