package org.orbit.component.connector;

import org.orbit.component.api.ComponentConstants;
import org.orbit.infra.api.InfraConstants;
import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends AbstractBundleActivator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static Activator instance;

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		Activator.instance = this;

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		Activator.instance = null;

		super.stop(bundleContext);
	}

	@Override
	protected String[] getPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				ComponentConstants.ORBIT_USER_ACCOUNTS_URL, //
				ComponentConstants.ORBIT_AUTH_URL, //
				ComponentConstants.ORBIT_REGISTRY_URL, //
				ComponentConstants.ORBIT_APP_STORE_URL, //
				ComponentConstants.ORBIT_DOMAIN_SERVICE_URL, //
				ComponentConstants.ORBIT_NODE_CONTROL_URL, //
				ComponentConstants.ORBIT_MISSION_CONTROL_URL, //
		};
		return propNames;
	}

}
