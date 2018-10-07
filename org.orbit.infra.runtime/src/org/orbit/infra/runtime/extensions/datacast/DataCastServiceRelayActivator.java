package org.orbit.infra.runtime.extensions.datacast;

import java.util.Map;

import org.orbit.infra.runtime.lb.DataCastWSApplicationRelay;
import org.orbit.infra.runtime.lb.InfraRelays;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.osgi.framework.BundleContext;

public class DataCastServiceRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.DataCastServiceRelayActivator";

	public static DataCastServiceRelayActivator INSTANCE = new DataCastServiceRelayActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> initProperties = context.getProperties();

		// Start DataCastService relay
		DataCastWSApplicationRelay relay = InfraRelays.getInstance().createDataCastRelay(bundleContext, initProperties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(DataCastWSApplicationRelay.class, relay);
		}
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop DataCastService relay
		DataCastWSApplicationRelay relay = process.getAdapter(DataCastWSApplicationRelay.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
