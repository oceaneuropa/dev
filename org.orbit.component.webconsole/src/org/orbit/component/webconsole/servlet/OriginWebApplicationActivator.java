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
public class OriginWebApplicationActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.webconsole.OriginWebApplicationActivator";

	public static OriginWebApplicationActivator INSTANCE = new OriginWebApplicationActivator();

	@Override
	public void start(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		OriginWebApplication webApp = new OriginWebApplication(properties);
		webApp.start(bundleContext);

		// WorldWebApplication worldApp = new WorldWebApplication(properties);
		// worldApp.start(bundleContext);

		process.adapt(OriginWebApplication.class, webApp);
		// process.adapt(WorldWebApplication.class, worldApp);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		OriginWebApplication webApp = process.getAdapter(OriginWebApplication.class);
		if (webApp != null) {
			webApp.stop(bundleContext);
		}

		// WorldWebApplication worldApp = process.getAdapter(WorldWebApplication.class);
		// if (worldApp != null) {
		// worldApp.stop(bundleContext);
		// }
	}

}
