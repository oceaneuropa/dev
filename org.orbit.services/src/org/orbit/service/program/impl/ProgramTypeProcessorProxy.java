package org.orbit.service.program.impl;

import org.orbit.service.program.ProgramProvider;
import org.orbit.service.program.ProgramService;
import org.orbit.service.program.ProgramTypeProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ProgramTypeProcessorProxy implements ProgramTypeProcessor {

	protected BundleContext context;
	protected ServiceReference<?> reference;
	protected String programTypeId;

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param programTypeId
	 */
	public ProgramTypeProcessorProxy(BundleContext context, ServiceReference<?> reference, String programTypeId) {
		this.context = context;
		this.reference = reference;
		this.programTypeId = programTypeId;
	}

	@Override
	public String getProgramTypeId() {
		return this.programTypeId;
	}

	protected Object getTarget() {
		return (this.context != null) ? this.context.getService(this.reference) : null;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getTarget(Class<T> targetClass) {
		T target = null;
		if (targetClass != null) {
			Object service = getTarget();
			if (service != null && targetClass.isAssignableFrom(service.getClass())) {
				target = (T) service;
			}
		}
		return target;
	}

	protected void ungetTarget() {
		if (this.context != null) {
			try {
				this.context.ungetService(this.reference);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public ProgramProvider[] getProgramProviders() {
		ProgramProvider[] providers = null;
		try {
			ProgramTypeProcessor processor = getTarget(ProgramTypeProcessor.class);
			if (processor != null) {
				providers = processor.getProgramProviders();
			}
		} finally {
			ungetTarget();
		}
		if (providers == null) {
			providers = ProgramService.EMPTY_ARRAY;
		}
		return providers;
	}

	@Override
	public ProgramProvider getProgramProvider(String programProviderId) {
		ProgramProvider provider = null;
		try {
			ProgramTypeProcessor processor = getTarget(ProgramTypeProcessor.class);
			if (processor != null) {
				provider = processor.getProgramProvider(programProviderId);
			}
		} finally {
			ungetTarget();
		}
		return provider;
	}

}
