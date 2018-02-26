package org.orbit.platform.sdk.extension.impl;

import java.util.Hashtable;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionProcessor;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.orbit.platform.sdk.extension.util.ProgramExtensionTracker;
import org.orbit.platform.sdk.extension.util.ProgramExtensionTracker.ProgramExtensionFilter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public class ProgramExtensionProcessorImpl implements IProgramExtensionProcessor {

	protected String typeId;
	protected ProgramExtensionTracker programExtensionTracker;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param typeId
	 *            Extension type Id.
	 */
	public ProgramExtensionProcessorImpl(String typeId) {
		this.typeId = checkTypeId(typeId);
	}

	protected String checkTypeId(String typeId) {
		if (typeId == null) {
			throw new IllegalArgumentException("typeId is null");
		}
		return typeId;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public IProgramExtension[] getExtensions() {
		IProgramExtension[] programExtensions = null;
		if (this.programExtensionTracker != null) {
			programExtensions = this.programExtensionTracker.getProgramExtensions();
		}
		if (programExtensions == null) {
			programExtensions = IProgramExtensionService.EMPTY_ARRAY;
		}
		return programExtensions;
	}

	@Override
	public IProgramExtension getExtension(String extensionId) {
		IProgramExtension programExtension = null;
		if (this.programExtensionTracker != null && extensionId != null) {
			programExtension = this.programExtensionTracker.getProgramExtension(extensionId);
		}
		return programExtension;
	}

	/**
	 * 
	 * @param context
	 */
	public void start(final BundleContext context) {
		// Start tracking ProgramExtension services
		this.programExtensionTracker = new ProgramExtensionTracker();
		this.programExtensionTracker.setFilter(new ProgramExtensionFilter() {
			@Override
			public boolean accept(ServiceReference<IProgramExtension> reference) {
				String typeId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_TYPE_ID);
				if (getTypeId().equals(typeId)) {
					return true;
				}
				return false;
			}
		});
		this.programExtensionTracker.start(context);

		// Register as a ProgramExtensionProcessor service.
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(IProgramExtensionService.PROP_EXTENSION_TYPE_ID, getTypeId());
		this.serviceRegistration = context.registerService(IProgramExtensionProcessor.class, this, props);
	}

	/**
	 * 
	 * @param context
	 */
	public void stop(final BundleContext context) {
		// Unregister as a ProgramExtensionProcessor service.
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		// Stop tracking ProgramExtension services
		if (this.programExtensionTracker != null) {
			this.programExtensionTracker.stop(context);
			this.programExtensionTracker = null;
		}
	}

}
