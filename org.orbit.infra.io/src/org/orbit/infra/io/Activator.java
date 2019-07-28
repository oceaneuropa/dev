package org.orbit.infra.io;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.instance = null;
		Activator.context = null;
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
