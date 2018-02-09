package org.orbit.service.program.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.service.program.IProgramExtension;
import org.orbit.service.program.IProgramExtensionService;
import org.orbit.service.program.impl.ProgramExtensionProxy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramExtensionTracker {

	protected String typeId;
	protected ServiceTracker<IProgramExtension, IProgramExtension> serviceTracker;
	protected Map<String, IProgramExtension> programExtensionMap = new HashMap<String, IProgramExtension>();

	/**
	 * 
	 * @param typeId
	 */
	public ProgramExtensionTracker(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public IProgramExtension[] getProgramExtensions() {
		return this.programExtensionMap.values().toArray(new IProgramExtension[this.programExtensionMap.size()]);
	}

	public IProgramExtension getProgramExtension(String extensionId) {
		IProgramExtension programExtension = null;
		if (extensionId != null) {
			programExtension = this.programExtensionMap.get(extensionId);
		}
		return programExtension;
	}

	/**
	 * 
	 * @param context
	 */
	public void start(final BundleContext context) {
		// clean up data
		this.programExtensionMap.clear();

		// Start tracking ProgramExtension services
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
		// Stop tracking ProgramExtension services
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}

		// clean up data
		this.programExtensionMap.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void serviceAdded(BundleContext context, ServiceReference<IProgramExtension> reference) {
		String typeId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_TYPE_ID);
		String extensionId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_ID);
		if (this.typeId.equals(typeId) && extensionId != null) {
			ProgramExtensionProxy proxy = new ProgramExtensionProxy(context, reference, typeId, extensionId);
			this.programExtensionMap.put(extensionId, proxy);
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
		if (this.typeId.equals(typeId) && extensionId != null) {
			this.programExtensionMap.remove(extensionId);
		}
	}

}
