package org.origin.common.launch.util;

import org.origin.common.launch.LaunchService;
import org.origin.common.service.AbstractServiceTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class LaunchServiceTracker extends AbstractServiceTracker<LaunchService> {

	@Override
	protected ServiceTracker<LaunchService, LaunchService> createServiceTracker(BundleContext bundleContext) {
		ServiceTracker<LaunchService, LaunchService> serviceTracker = new ServiceTracker<LaunchService, LaunchService>(bundleContext, LaunchService.class, new ServiceTrackerCustomizer<LaunchService, LaunchService>() {
			@Override
			public LaunchService addingService(ServiceReference<LaunchService> reference) {
				LaunchService service = bundleContext.getService(reference);
				notifyServiceAdded(service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<LaunchService> reference, LaunchService service) {
			}

			@Override
			public void removedService(ServiceReference<LaunchService> reference, LaunchService service) {
				notifyServiceRemoved(service);
			}
		});
		return serviceTracker;
	}

}
