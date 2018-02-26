package org.orbit.platform.sdk.extension.impl;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionProcessor;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ProgramExtensionProcessorProxy implements IProgramExtensionProcessor {

	protected BundleContext context;
	protected ServiceReference<?> reference;
	protected String typeId;

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param typeId
	 */
	public ProgramExtensionProcessorProxy(BundleContext context, ServiceReference<?> reference, String typeId) {
		this.context = context;
		this.reference = reference;
		this.typeId = typeId;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
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
	public IProgramExtension[] getExtensions() {
		IProgramExtension[] programExtensions = null;
		try {
			IProgramExtensionProcessor processor = getTarget(IProgramExtensionProcessor.class);
			if (processor != null) {
				programExtensions = processor.getExtensions();
			}
		} finally {
			ungetTarget();
		}
		if (programExtensions == null) {
			programExtensions = IProgramExtensionService.EMPTY_ARRAY;
		}
		return programExtensions;
	}

	@Override
	public IProgramExtension getExtension(String extensionId) {
		IProgramExtension programExtension = null;
		try {
			IProgramExtensionProcessor processor = getTarget(IProgramExtensionProcessor.class);
			if (processor != null) {
				programExtension = processor.getExtension(extensionId);
			}
		} finally {
			ungetTarget();
		}
		return programExtension;
	}

}
