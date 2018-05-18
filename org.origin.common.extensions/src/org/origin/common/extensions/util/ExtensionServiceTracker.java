package org.origin.common.extensions.util;

import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.service.AbstractServiceTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ExtensionServiceTracker extends AbstractServiceTracker<IExtensionService> {

	@Override
	protected ServiceTracker<IExtensionService, IExtensionService> createServiceTracker(final BundleContext bundleContext) {
		ServiceTracker<IExtensionService, IExtensionService> serviceTracker = new ServiceTracker<IExtensionService, IExtensionService>(bundleContext, IExtensionService.class, new ServiceTrackerCustomizer<IExtensionService, IExtensionService>() {
			@Override
			public IExtensionService addingService(ServiceReference<IExtensionService> reference) {
				IExtensionService service = bundleContext.getService(reference);
				notifyServiceAdded(service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<IExtensionService> reference, IExtensionService service) {
			}

			@Override
			public void removedService(ServiceReference<IExtensionService> reference, IExtensionService service) {
				notifyServiceRemoved(service);
			}
		});
		return serviceTracker;
	}

}
