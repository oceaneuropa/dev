/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk.extension.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.platform.sdk.extension.IProgramExtensionProcessor;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.orbit.platform.sdk.extension.impl.ProgramExtensionProcessorProxy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramExtensionProcessorTracker {

	protected ServiceTracker<IProgramExtensionProcessor, IProgramExtensionProcessor> serviceTracker;
	protected Map<String, IProgramExtensionProcessor> typeIdToProcessorMap = new HashMap<String, IProgramExtensionProcessor>();

	/**
	 * 
	 * @param extensionTypeId
	 * @return
	 */
	public IProgramExtensionProcessor getProcessor(String extensionTypeId) {
		IProgramExtensionProcessor processor = this.typeIdToProcessorMap.get(extensionTypeId);
		return processor;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.typeIdToProcessorMap.clear();

		this.serviceTracker = new ServiceTracker<IProgramExtensionProcessor, IProgramExtensionProcessor>(bundleContext, IProgramExtensionProcessor.class, new ServiceTrackerCustomizer<IProgramExtensionProcessor, IProgramExtensionProcessor>() {
			@Override
			public IProgramExtensionProcessor addingService(ServiceReference<IProgramExtensionProcessor> reference) {
				IProgramExtensionProcessor processor = bundleContext.getService(reference);
				serviceAdded(bundleContext, reference);
				notifyServiceAdded(processor, 1000);
				return processor;
			}

			@Override
			public void modifiedService(ServiceReference<IProgramExtensionProcessor> reference, IProgramExtensionProcessor processor) {
			}

			@Override
			public void removedService(ServiceReference<IProgramExtensionProcessor> reference, IProgramExtensionProcessor processor) {
				serviceRemoved(bundleContext, reference);
				notifyServiceRemoved(processor, 1000);
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

		this.typeIdToProcessorMap.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void serviceAdded(BundleContext context, ServiceReference<IProgramExtensionProcessor> reference) {
		String typeId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_TYPE_ID);
		if (typeId != null) {
			ProgramExtensionProcessorProxy proxy = new ProgramExtensionProcessorProxy(context, reference, typeId);
			this.typeIdToProcessorMap.put(typeId, proxy);
		}
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void serviceRemoved(BundleContext context, ServiceReference<IProgramExtensionProcessor> reference) {
		String typeId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_TYPE_ID);
		if (typeId != null) {
			this.typeIdToProcessorMap.remove(typeId);
		}
	}

	protected void notifyServiceAdded(final IProgramExtensionProcessor processor, final long delay) {
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
				notifyServiceAdded(processor);
			}
		});
		t.start();
	}

	protected void notifyServiceRemoved(final IProgramExtensionProcessor processor, final long delay) {
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
				notifyServiceRemoved(processor);
			}
		});
		t.start();
	}

	protected void notifyServiceAdded(IProgramExtensionProcessor processor) {

	}

	protected void notifyServiceRemoved(IProgramExtensionProcessor processor) {

	}

}
