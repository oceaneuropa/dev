package org.orbit.sprit.runtime.gaia.extensions;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.sprit.runtime.gaia.service.GAIA;
import org.orbit.sprit.runtime.gaia.service.GAIAImpl;
import org.osgi.framework.BundleContext;

public class GAIAServiceActivator implements ServiceActivator {

	public static final String ID = "component.gaia.service_activator";

	public static GAIAServiceActivator INSTANCE = new GAIAServiceActivator();

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

// @Override
// public String getName() {
// return "GAIA";
// }

// @Descriptor("Start GAIA")
// public void startGAIA() throws ClientException {
// LOG.info("startGAIA()");
//
// if (this.gaiaImpl == null) {
//// Properties configIniProps = SetupUtil.getNodeHomeConfigIniProperties(this.bundleContext);
// this.gaiaImpl = new GAIAImpl(this.bundleContext, null);
// }
// this.gaiaImpl.start();
// }
//
// @Descriptor("Stop GAIA")
// public void stopGAIA() throws ClientException {
// LOG.info("stopGAIA()");
//
// if (this.gaiaImpl != null) {
// this.gaiaImpl.stop();
// this.gaiaImpl = null;
// }
// }
// protected void checkGAIA() throws ClientException {
// if (this.gaia == null) {
// LOG.info("NodeOS is not available.");
// throw new ClientException(500, "GAIA is not available.");
// }
// }
