package org.orbit.infra.runtime.extensions.datatube;

import java.util.Map;

import org.orbit.infra.runtime.lb.DataTubeWSApplicationRelay;
import org.orbit.infra.runtime.lb.InfraRelays;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.osgi.framework.BundleContext;

public class DataTubeServiceRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.DataTubeServiceRelayActivator";

	public static DataTubeServiceRelayActivator INSTANCE = new DataTubeServiceRelayActivator();

	@Override
	public void start(IProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> initProperties = context.getProperties();

		// Start DataTubeService relay
		DataTubeWSApplicationRelay relay = InfraRelays.getInstance().createDataTubeRelay(bundleContext, initProperties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(DataTubeWSApplicationRelay.class, relay);
		}
	}

	@Override
	public void stop(IProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop DataTubeService relay
		DataTubeWSApplicationRelay relay = process.getAdapter(DataTubeWSApplicationRelay.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
