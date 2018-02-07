package org.orbit.service.program.impl;

import java.util.Hashtable;

import org.orbit.service.program.ProgramProvider;
import org.orbit.service.program.ProgramTypeProcessor;
import org.orbit.service.program.ProgramService;
import org.orbit.service.program.ProgramProviderTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ProgramTypeProcessorImpl implements ProgramTypeProcessor {

	protected String programTypeId;
	protected ProgramProviderTracker programProviderTracker;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param programTypeId
	 */
	public ProgramTypeProcessorImpl(String programTypeId) {
		this.programTypeId = programTypeId;
	}

	@Override
	public String getProgramTypeId() {
		return this.programTypeId;
	}

	@Override
	public ProgramProvider[] getProgramProviders() {
		ProgramProvider[] providers = null;
		if (this.programProviderTracker != null) {
			providers = this.programProviderTracker.getProgramProviders();
		}
		if (providers == null) {
			providers = ProgramService.EMPTY_ARRAY;
		}
		return providers;
	}

	@Override
	public ProgramProvider getProgramProvider(String providerId) {
		ProgramProvider provider = null;
		if (this.programProviderTracker != null && providerId != null) {
			provider = this.programProviderTracker.getProgramProvider(providerId);
		}
		return provider;
	}

	/**
	 * 
	 * @param context
	 */
	public void start(final BundleContext context) {
		this.programProviderTracker = new ProgramProviderTracker(this.programTypeId);
		this.programProviderTracker.start(context);

		// Register as a ProgramProviderProcessor service.
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(ProgramService.PROP_PROGRAM_TYPE_ID, getProgramTypeId());
		this.serviceRegistration = context.registerService(ProgramTypeProcessor.class, this, props);
	}

	/**
	 * 
	 * @param context
	 */
	public void stop(final BundleContext context) {
		// Unregister as a ProgramProviderProcessor service.
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		// Stop tracking ProgramProvider services
		if (this.programProviderTracker != null) {
			this.programProviderTracker.stop(context);
			this.programProviderTracker = null;
		}
	}

}
