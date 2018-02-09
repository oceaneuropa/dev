package org.orbit.service.program.util;

import org.orbit.service.program.IProgramExtensionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramExtensionServiceTracker {

	protected ServiceTracker<IProgramExtensionService, IProgramExtensionService> serviceTracker;

	public IProgramExtensionService getProgramService() {
		IProgramExtensionService programExtensionService = null;
		if (this.serviceTracker != null) {
			programExtensionService = this.serviceTracker.getService();
		}
		return programExtensionService;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<IProgramExtensionService, IProgramExtensionService>(bundleContext, IProgramExtensionService.class, new ServiceTrackerCustomizer<IProgramExtensionService, IProgramExtensionService>() {
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
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	protected void notifyServiceAdded(final IProgramExtensionService programExtensionService, final long delay) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (delay > 0) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				notifyServiceAdded(programExtensionService);
			}
		});
		t.start();
	}

	protected void notifyServiceRemoved(final IProgramExtensionService programExtensionService, final long delay) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (delay > 0) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				notifyServiceRemoved(programExtensionService);
			}
		});
		t.start();
	}

	protected void notifyServiceAdded(IProgramExtensionService programExtensionService) {

	}

	protected void notifyServiceRemoved(IProgramExtensionService programExtensionService) {

	}

}
