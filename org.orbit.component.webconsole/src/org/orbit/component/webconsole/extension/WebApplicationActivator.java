/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.webconsole.extension;

import java.util.Map;

import org.orbit.component.webconsole.servlet.WebApplication;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.osgi.framework.BundleContext;

public class WebApplicationActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.webconsole.WebApplicationActivator";

	public static WebApplicationActivator INSTANCE = new WebApplicationActivator();

	@Override
	public void start(IProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		WebApplication webApp = new WebApplication(properties);
		webApp.start(bundleContext);

		process.adapt(WebApplication.class, webApp);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		WebApplication webApp = process.getAdapter(WebApplication.class);
		if (webApp != null) {
			webApp.stop(bundleContext);
		}
	}

}
