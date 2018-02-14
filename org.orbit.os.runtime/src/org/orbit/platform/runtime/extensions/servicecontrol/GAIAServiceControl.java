package org.orbit.platform.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.platform.runtime.gaia.service.impl.GAIAImpl;
import org.orbit.platform.sdk.servicecontrol.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class GAIAServiceControl extends ServiceControlImpl {

	public static GAIAServiceControl INSTANCE = new GAIAServiceControl();

	protected GAIAImpl gaiaImpl;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		this.gaiaImpl = new GAIAImpl(bundleContext, null);
		this.gaiaImpl.start();
	}

	@Override
	public void stop(BundleContext bundleContext, Map<String, Object> properties) {
		if (this.gaiaImpl != null) {
			this.gaiaImpl.stop();
			this.gaiaImpl = null;
		}
	}

}
