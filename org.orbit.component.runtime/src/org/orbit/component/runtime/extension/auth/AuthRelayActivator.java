package org.orbit.component.runtime.extension.auth;

import java.util.Map;

import org.orbit.component.runtime.OrbitRelays;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.origin.common.rest.server.WSRelayApplication;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class AuthRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.AuthRelayActivator";

	public static AuthRelayActivator INSTANCE = new AuthRelayActivator();

	@Override
	public void start(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start relay
		WSRelayApplication relay = OrbitRelays.getInstance().createAuthRelay(bundleContext, properties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(WSRelayApplication.class, relay);
		}
	}

	@Override
	public void stop(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop relay
		WSRelayApplication relay = process.getAdapter(WSRelayApplication.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
