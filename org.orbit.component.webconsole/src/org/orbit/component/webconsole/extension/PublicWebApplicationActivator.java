/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.webconsole.extension;

import java.util.Map;

import org.orbit.component.webconsole.servlet.PublicWebApplication;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.osgi.framework.BundleContext;

public class PublicWebApplicationActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.webconsole.PublicWebApplicationActivator";

	public static PublicWebApplicationActivator INSTANCE = new PublicWebApplicationActivator();

	@Override
	public void start(IProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		PublicWebApplication webApp = new PublicWebApplication(properties);
		webApp.start(bundleContext);

		process.adapt(PublicWebApplication.class, webApp);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		PublicWebApplication webApp = process.getAdapter(PublicWebApplication.class);
		if (webApp != null) {
			webApp.stop(bundleContext);
		}
	}

}
