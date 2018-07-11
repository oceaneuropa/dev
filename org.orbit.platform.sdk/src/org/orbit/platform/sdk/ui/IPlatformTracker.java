package org.orbit.platform.sdk.ui;

import org.orbit.platform.sdk.IPlatform;
import org.origin.common.service.AbstractServiceTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class IPlatformTracker extends AbstractServiceTracker<IPlatform> {

	@Override
	protected ServiceTracker<IPlatform, IPlatform> createServiceTracker(final BundleContext bundleContext) {
		ServiceTracker<IPlatform, IPlatform> serviceTracker = new ServiceTracker<IPlatform, IPlatform>(bundleContext, IPlatform.class, new ServiceTrackerCustomizer<IPlatform, IPlatform>() {
			@Override
			public IPlatform addingService(ServiceReference<IPlatform> reference) {
				IPlatform platform = bundleContext.getService(reference);
				notifyServiceAdded(platform);
				return platform;
			}

			@Override
			public void modifiedService(ServiceReference<IPlatform> reference, IPlatform platform) {
			}

			@Override
			public void removedService(ServiceReference<IPlatform> reference, IPlatform platform) {
				notifyServiceRemoved(platform);
			}
		});
		return serviceTracker;
	}

}
