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
public class MainWebApplicationActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.webconsole.MainWebApplicationActivator";

	public static MainWebApplicationActivator INSTANCE = new MainWebApplicationActivator();

	@Override
	public void start(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		MainWebApplication webApp = new MainWebApplication(properties);
		webApp.start(bundleContext);

		process.adapt(MainWebApplication.class, webApp);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		MainWebApplication webApp = process.getAdapter(MainWebApplication.class);
		if (webApp != null) {
			webApp.stop(bundleContext);
		}
	}

}
