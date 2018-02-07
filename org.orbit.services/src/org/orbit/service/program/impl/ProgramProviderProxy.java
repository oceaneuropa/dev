package org.orbit.service.program.impl;

import org.orbit.service.program.ProgramInstance;
import org.orbit.service.program.ProgramProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ProgramProviderProxy implements ProgramProvider {

	protected BundleContext context;
	protected ServiceReference<?> reference;
	protected String programTypeId;
	protected String programProviderId;

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param programTypeId
	 *            program type id. e.g. "orbit.switcher"
	 * @param programProviderId
	 *            program provider id. e.g. "UserRegistrySwitcher", "AuthSwitcher", "AppStoreSwitcher", etc.
	 */
	public ProgramProviderProxy(BundleContext context, ServiceReference<?> reference, String programTypeId, String programProviderId) {
		this.context = context;
		this.reference = reference;
		this.programTypeId = programTypeId;
		this.programProviderId = programProviderId;
	}

	@Override
	public String getTypeId() {
		return this.programTypeId;
	}

	@Override
	public String getId() {
		return this.programProviderId;
	}

	protected Object getTarget() {
		return (this.context != null) ? this.context.getService(this.reference) : null;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getTarget(Class<T> targetClass) {
		T target = null;
		if (targetClass != null) {
			Object targetObject = getTarget();
			if (targetObject != null && targetClass.isAssignableFrom(targetObject.getClass())) {
				target = (T) targetObject;
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
	public Object launch(BundleContext context) {
		ProgramInstanceImpl programInstance = null;
		Object object = null;
		try {
			ProgramProvider provider = getTarget(ProgramProvider.class);
			if (provider != null) {
				object = provider.launch(context);
				programInstance = new ProgramInstanceImpl(context, this, object);
				// Note:
				// - No provider, no instance.
				// - Assign instance Id to it, maybe?
			}
		} finally {
			ungetTarget();
		}
		return programInstance;
	}

	@Override
	public int exit(BundleContext context, Object instance) {
		int exitCode = 0;
		try {
			ProgramProvider provider = getTarget(ProgramProvider.class);
			if (provider != null) {
				Object object = instance;
				if (instance instanceof ProgramInstance) {
					ProgramInstanceImpl programInstance = (ProgramInstanceImpl) instance;
					// Note:
					// - Do some clean up, maybe?
					object = programInstance.getReferenceInstance();
				}
				exitCode = provider.exit(context, object);
			}
		} finally {
			ungetTarget();
		}
		return exitCode;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		try {
			ProgramProvider provider = getTarget(ProgramProvider.class);
			if (provider != null) {
				provider.adapt(clazz, object);
			}
		} finally {
			ungetTarget();
		}
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T t = null;
		try {
			ProgramProvider provider = getTarget(ProgramProvider.class);
			if (provider != null) {
				t = provider.getAdapter(adapter);
			}
		} finally {
			ungetTarget();
		}
		return t;
	}

}
