package org.orbit.component.webconsole.servlet;

import java.util.Map;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class PublicWebApplicationActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.webconsole.PublicWebApplicationActivator";

	public static PublicWebApplicationActivator INSTANCE = new PublicWebApplicationActivator();

	@Override
	public void start(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		PublicWebApplication webApp = new PublicWebApplication(properties);
		webApp.start(bundleContext);

		process.adapt(PublicWebApplication.class, webApp);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		PublicWebApplication webApp = process.getAdapter(PublicWebApplication.class);
		if (webApp != null) {
			webApp.stop(bundleContext);
		}
	}

}
