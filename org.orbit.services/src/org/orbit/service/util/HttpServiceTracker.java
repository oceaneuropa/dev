package org.orbit.service.util;

import org.origin.common.service.AbstractServiceTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class HttpServiceTracker extends AbstractServiceTracker<HttpService> {

	@Override
	protected ServiceTracker<HttpService, HttpService> createServiceTracker(BundleContext bundleContext) {
		ServiceTracker<HttpService, HttpService> serviceTracker = new ServiceTracker<HttpService, HttpService>(bundleContext, HttpService.class, new ServiceTrackerCustomizer<HttpService, HttpService>() {
			@Override
			public HttpService addingService(ServiceReference<HttpService> reference) {
				HttpService service = bundleContext.getService(reference);
				notifyServiceAdded(service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<HttpService> reference, HttpService service) {
			}

			@Override
			public void removedService(ServiceReference<HttpService> reference, HttpService service) {
				notifyServiceRemoved(service);
			}
		});
		return serviceTracker;
	}

}
