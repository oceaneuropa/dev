/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk.extension.util;

import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.origin.common.service.AbstractServiceTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramExtensionServiceTracker extends AbstractServiceTracker<IProgramExtensionService> {

	@Override
	protected ServiceTracker<IProgramExtensionService, IProgramExtensionService> createServiceTracker(final BundleContext bundleContext) {
		ServiceTracker<IProgramExtensionService, IProgramExtensionService> serviceTracker = new ServiceTracker<IProgramExtensionService, IProgramExtensionService>(bundleContext, IProgramExtensionService.class, new ServiceTrackerCustomizer<IProgramExtensionService, IProgramExtensionService>() {
			@Override
			public IProgramExtensionService addingService(ServiceReference<IProgramExtensionService> reference) {
				IProgramExtensionService programService = bundleContext.getService(reference);
				notifyServiceAdded(programService, 1000);
				return programService;
			}

			@Override
			public void modifiedService(ServiceReference<IProgramExtensionService> reference, IProgramExtensionService programService) {
			}

			@Override
			public void removedService(ServiceReference<IProgramExtensionService> reference, IProgramExtensionService programService) {
				notifyServiceRemoved(programService, 1000);
			}
		});
		return serviceTracker;
	}

}
