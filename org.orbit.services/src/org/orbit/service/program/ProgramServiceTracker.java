package org.orbit.service.program;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramServiceTracker {

	protected ServiceTracker<ProgramService, ProgramService> serviceTracker;

	public ProgramService getProgramService() {
		ProgramService programService = null;
		if (this.serviceTracker != null) {
			programService = this.serviceTracker.getService();
		}
		return programService;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<ProgramService, ProgramService>(bundleContext, ProgramService.class, new ServiceTrackerCustomizer<ProgramService, ProgramService>() {
			@Override
			public ProgramService addingService(ServiceReference<ProgramService> reference) {
				ProgramService programService = bundleContext.getService(reference);
				notifyProgramServiceAdded(programService, 1000);
				return programService;
			}

			@Override
			public void modifiedService(ServiceReference<ProgramService> reference, ProgramService programService) {
			}

			@Override
			public void removedService(ServiceReference<ProgramService> reference, ProgramService programService) {
				notifyProgramServiceRemoved(programService, 1000);
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

	protected void notifyProgramServiceAdded(final ProgramService programService, final long delay) {
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
				notifyProgramServiceAdded(programService);
			}
		});
		t.start();
	}

	protected void notifyProgramServiceRemoved(final ProgramService programService, final long delay) {
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
				notifyProgramServiceRemoved(programService);
			}
		});
		t.start();
	}

	protected void notifyProgramServiceAdded(ProgramService programService) {

	}

	protected void notifyProgramServiceRemoved(ProgramService programService) {

	}

}
