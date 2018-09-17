package org.orbit.spirit.runtime.extension;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.orbit.spirit.runtime.gaia.service.impl.GaiaServiceImpl;
import org.osgi.framework.BundleContext;

public class GaiaServiceActivator implements ServiceActivator {

	public static final String ID = "component.gaia.service_activator";

	public static GaiaServiceActivator INSTANCE = new GaiaServiceActivator();

	@Override
	public synchronized void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start GAIA service
		GaiaServiceImpl newGAIA = new GaiaServiceImpl(properties);
		newGAIA.start(bundleContext);

		process.adapt(GaiaService.class, newGAIA);
	}

	@Override
	public synchronized void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop GAIA service
		GaiaService gaia = process.getAdapter(GaiaService.class);
		if (gaia instanceof GaiaServiceImpl) {
			((GaiaServiceImpl) gaia).stop(bundleContext);
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
