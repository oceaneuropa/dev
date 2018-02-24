package org.orbit.platform.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.platform.runtime.gaia.service.GAIA;
import org.orbit.platform.runtime.gaia.service.impl.GAIAImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivatorImpl;
import org.osgi.framework.BundleContext;

public class GAIAServiceActivator extends ServiceActivatorImpl {

	public static final String ID = "component.gaia.service_activator";

	public static GAIAServiceActivator INSTANCE = new GAIAServiceActivator();

	@Override
	public String getProcessName() {
		return "GAIA";
	}

	@Override
	public synchronized void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<String, Object> properties = context.getProperties();

		// Start GAIA service
		GAIAImpl newGAIA = new GAIAImpl(properties);
		newGAIA.start(bundleContext);

		process.adapt(GAIA.class, newGAIA);
	}

	@Override
	public synchronized void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop GAIA service
		GAIA gaia = process.getAdapter(GAIA.class);
		if (gaia instanceof GAIAImpl) {
			((GAIAImpl) gaia).stop(bundleContext);
		}
	}

}
