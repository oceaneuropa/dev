package org.orbit.service.program.impl;

import java.util.Map;

import org.orbit.service.program.IProgramExtension;
import org.orbit.service.program.IProgramExtensionFilter;
import org.orbit.service.program.IProgramLauncher;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ProgramExtensionProxy implements IProgramExtension {

	protected BundleContext context;
	protected ServiceReference<?> reference;
	protected String typeId;
	protected String id;

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param typeId
	 *            extension type id. e.g. "orbit.switcher"
	 * @param id
	 *            extension id. e.g. "UserRegistrySwitcher", "AuthSwitcher", "AppStoreSwitcher", etc.
	 */
	public ProgramExtensionProxy(BundleContext context, ServiceReference<?> reference, String typeId, String id) {
		this.context = context;
		this.reference = reference;
		this.typeId = typeId;
		this.id = id;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public String getId() {
		return this.id;
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
	public Map<Object, Object> getProperties() {
		Map<Object, Object> properties = null;
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				properties = programExtension.getProperties();
			}
		} finally {
			ungetTarget();
		}
		return properties;
	}

	@Override
	public IProgramLauncher getLauncher() {
		IProgramLauncher launcher = null;
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				launcher = programExtension.getLauncher();
			}
		} finally {
			ungetTarget();
		}
		return launcher;
	}

	@Override
	public IProgramExtensionFilter getFilter() {
		IProgramExtensionFilter filter = null;
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				filter = programExtension.getFilter();
			}
		} finally {
			ungetTarget();
		}
		return filter;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				programExtension.adapt(clazz, object);
			}
		} finally {
			ungetTarget();
		}
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T t = null;
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				t = programExtension.getAdapter(adapter);
			}
		} finally {
			ungetTarget();
		}
		return t;
	}

}
