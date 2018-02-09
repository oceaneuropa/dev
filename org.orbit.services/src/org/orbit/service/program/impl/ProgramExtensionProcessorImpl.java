package org.orbit.service.program.impl;

import java.util.Hashtable;

import org.orbit.service.program.IProgramExtension;
import org.orbit.service.program.IProgramExtensionProcessor;
import org.orbit.service.program.IProgramExtensionService;
import org.orbit.service.program.util.ProgramExtensionTracker;
import org.osgi.framework.BundleContext;
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
		this.typeId = typeId;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public IProgramExtension[] getProgramExtensions() {
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
	public IProgramExtension getProgramExtension(String extensionId) {
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
		this.programExtensionTracker = new ProgramExtensionTracker(this.typeId);
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
