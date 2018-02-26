package org.orbit.platform.sdk.extension.util;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramExtensionTypeTracker {

	protected ServiceTracker<IProgramExtension, IProgramExtension> serviceTracker;
	protected SortedSet<String> extensionTypeIds = new TreeSet<String>();

	public Set<String> getExtensionTypeIds() {
		return this.extensionTypeIds;
	}

	/**
	 * 
	 * @param context
	 */
	public void start(final BundleContext context) {
		// clean up data
		this.extensionTypeIds.clear();

		// Start tracking IProgramExtension services
		this.serviceTracker = new ServiceTracker<IProgramExtension, IProgramExtension>(context, IProgramExtension.class, new ServiceTrackerCustomizer<IProgramExtension, IProgramExtension>() {
			@Override
			public IProgramExtension addingService(ServiceReference<IProgramExtension> reference) {
				IProgramExtension programExtension = context.getService(reference);
				if (programExtension != null) {
					serviceAdded(context, reference);
				}
				return programExtension;
			}

			@Override
			public void modifiedService(ServiceReference<IProgramExtension> reference, IProgramExtension programExtension) {
			}

			@Override
			public void removedService(ServiceReference<IProgramExtension> reference, IProgramExtension programExtension) {
				serviceRemoved(context, reference);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param context
	 */
	public void stop(BundleContext context) {
		// Stop tracking IProgramExtension services
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}

		// clean up data
		this.extensionTypeIds.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void serviceAdded(BundleContext context, ServiceReference<IProgramExtension> reference) {
		String typeId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_TYPE_ID);
		String extensionId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_ID);
		if (typeId != null && extensionId != null) {
			this.extensionTypeIds.add(typeId);
		}
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void serviceRemoved(BundleContext context, ServiceReference<IProgramExtension> reference) {
		String typeId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_TYPE_ID);
		String extensionId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_ID);
		if (typeId != null && extensionId != null) {
			this.extensionTypeIds.remove(typeId);
		}
	}

}
