package org.orbit.infra.io;

import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends AbstractBundleActivator {

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
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		CFG.clear();

		Activator.instance = null;
		super.stop(bundleContext);
	}

}

// URIHandlerDFileImpl.INSTANCE.register();
// DfsURLStreamHandlerFactory.INSTANCE.register(bundleContext);

// DfsURLStreamHandlerFactory.INSTANCE.unregister(bundleContext);
// URIHandlerDFileImpl.INSTANCE.unregister();

// @Override
// protected String[] getPropertyNames() {
// String[] propNames = new String[] { //
// InfraConstants.ORBIT_INDEX_SERVICE_URL, //
// InfraConstants.ORBIT_CONFIG_REGISTRY_URL, //
// };
// return propNames;
// }
