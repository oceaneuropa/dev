package org.origin.common.rest.util;

import org.osgi.framework.BundleContext;

public interface LifecycleAware {

	void start(BundleContext bundleContext);

	void stop(BundleContext bundleContext);

}
