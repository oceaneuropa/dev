package org.orbit.service.util;

import org.orbit.service.servlet.WebApplication;
import org.origin.common.service.AbstractServiceTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class WebApplicationTracker extends AbstractServiceTracker<WebApplication> {

	@Override
	protected ServiceTracker<WebApplication, WebApplication> createServiceTracker(final BundleContext bundleContext) {
		ServiceTracker<WebApplication, WebApplication> serviceTracker = new ServiceTracker<WebApplication, WebApplication>(bundleContext, WebApplication.class, new ServiceTrackerCustomizer<WebApplication, WebApplication>() {
			@Override
			public WebApplication addingService(ServiceReference<WebApplication> reference) {
				WebApplication service = bundleContext.getService(reference);
				notifyServiceAdded(service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<WebApplication> reference, WebApplication service) {
			}

			@Override
			public void removedService(ServiceReference<WebApplication> reference, WebApplication service) {
				notifyServiceRemoved(service);
			}
		});
		return serviceTracker;
	}

}
