/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.origin.common.service;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractServiceTracker<SERVICE> {

	private ServiceTracker<SERVICE, SERVICE> serviceTracker;

	protected abstract ServiceTracker<SERVICE, SERVICE> createServiceTracker(BundleContext bundleContext);

	public SERVICE getService() {
		SERVICE service = null;
		if (this.serviceTracker != null) {
			service = this.serviceTracker.getService();
		}
		return service;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = createServiceTracker(bundleContext);
		if (this.serviceTracker != null) {
			this.serviceTracker.open();
		}
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

	protected void notifyServiceAdded(final SERVICE service, final long delay) {
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
				notifyServiceAdded(service);
			}
		});
		t.start();
	}

	protected void notifyServiceRemoved(final SERVICE service, final long delay) {
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
				notifyServiceRemoved(service);
			}
		});
		t.start();
	}

	protected void notifyServiceAdded(SERVICE service) {

	}

	protected void notifyServiceRemoved(SERVICE service) {

	}

}
