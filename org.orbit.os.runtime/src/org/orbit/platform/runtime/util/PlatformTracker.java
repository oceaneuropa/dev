package org.orbit.platform.runtime.util;

import org.orbit.platform.runtime.platform.Platform;
import org.origin.common.service.AbstractServiceTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class PlatformTracker extends AbstractServiceTracker<Platform> {

	@Override
	protected ServiceTracker<Platform, Platform> createServiceTracker(final BundleContext bundleContext) {
		ServiceTracker<Platform, Platform> serviceTracker = new ServiceTracker<Platform, Platform>(bundleContext, Platform.class, new ServiceTrackerCustomizer<Platform, Platform>() {
			@Override
			public Platform addingService(ServiceReference<Platform> reference) {
				Platform os = bundleContext.getService(reference);
				notifyServiceAdded(os, 1000);
				return os;
			}

			@Override
			public void modifiedService(ServiceReference<Platform> reference, Platform os) {
			}

			@Override
			public void removedService(ServiceReference<Platform> reference, Platform os) {
				notifyServiceRemoved(os, 1000);
			}
		});
		return serviceTracker;
	}

}
