package org.orbit.infra.runtime.extensions.indexservice;

import java.util.Map;

import org.orbit.infra.runtime.lb.IndexServiceWSApplicationRelay;
import org.orbit.infra.runtime.lb.InfraRelays;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.IProcess;
import org.osgi.framework.BundleContext;

public class IndexServiceRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.IndexServiceRelayActivator";

	public static IndexServiceRelayActivator INSTANCE = new IndexServiceRelayActivator();

	@Override
	public void start(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start IndexService relay
		IndexServiceWSApplicationRelay relay = InfraRelays.getInstance().createIndexServiceRelay(bundleContext, properties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(IndexServiceWSApplicationRelay.class, relay);
		}
	}

	@Override
	public void stop(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop IndexService relay
		IndexServiceWSApplicationRelay relay = process.getAdapter(IndexServiceWSApplicationRelay.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
