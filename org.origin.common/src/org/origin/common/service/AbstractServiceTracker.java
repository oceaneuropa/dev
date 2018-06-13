/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.origin.common.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

	public SERVICE[] getServices(Class<SERVICE> clazz) {
		SERVICE[] array = null;
		if (this.serviceTracker != null) {
			Object[] objects = this.serviceTracker.getServices();
			if (objects != null) {
				List<SERVICE> services = new ArrayList<SERVICE>();
				array = (SERVICE[]) Array.newInstance(clazz, objects.length);

				for (Object object : objects) {
					if (clazz.isAssignableFrom(object.getClass())) {
						SERVICE s = (SERVICE) object;
						services.add(s);
					}
				}
				services.toArray(array);
			}
		}

		return array;
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

	protected void notifyServiceAdded(final SERVICE service) {
		notifyServiceAdded(service, 1000);
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
				serviceAdded(service);
			}
		});
		t.start();
	}

	protected void notifyServiceRemoved(final SERVICE service) {
		notifyServiceRemoved(service, 1000);
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
				serviceRemoved(service);
			}
		});
		t.start();
	}

	protected void serviceAdded(SERVICE service) {

	}

	protected void serviceRemoved(SERVICE service) {

	}

}
