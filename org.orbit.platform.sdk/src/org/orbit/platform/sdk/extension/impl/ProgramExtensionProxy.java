package org.orbit.platform.sdk.extension.impl;

import java.util.Map;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionFilter;
import org.orbit.platform.sdk.extension.desc.InterfaceDescription;
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
	public String getName() {
		String name = null;
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				name = extension.getName();
			}
		} finally {
			ungetTarget();
		}
		return name;
	}

	@Override
	public String getDescription() {
		String description = null;
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				description = extension.getDescription();
			}
		} finally {
			ungetTarget();
		}
		return description;
	}

	@Override
	public Map<Object, Object> getProperties() {
		Map<Object, Object> properties = null;
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				properties = extension.getProperties();
			}
		} finally {
			ungetTarget();
		}
		return properties;
	}

	@Override
	public IProgramExtensionFilter getFilter() {
		IProgramExtensionFilter filter = null;
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				filter = extension.getFilter();
			}
		} finally {
			ungetTarget();
		}
		return filter;
	}

	@Override
	public Object[] getInterfaces() {
		Object[] interfaces = null;
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				interfaces = extension.getInterfaces();
			}
		} finally {
			ungetTarget();
		}
		return interfaces;
	}

	@Override
	public <T> T getInterface(Class<T> clazz) {
		T t = null;
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				t = extension.getInterface(clazz);
			}
		} finally {
			ungetTarget();
		}
		return t;
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Class<?> clazz) {
		InterfaceDescription desc = null;
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				desc = extension.getInterfaceDescription(clazz);
			}
		} finally {
			ungetTarget();
		}
		return desc;
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Object object) {
		InterfaceDescription desc = null;
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				desc = extension.getInterfaceDescription(object);
			}
		} finally {
			ungetTarget();
		}
		return desc;
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T object) {
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				extension.addInterface(clazz, object);
			}
		} finally {
			ungetTarget();
		}
	}

	@Override
	public <T> void addInterface(InterfaceDescription description) {
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				extension.addInterface(description);
			}
		} finally {
			ungetTarget();
		}
	}

	@Override
	public void removeInterface(Class<?>... classes) {
		try {
			IProgramExtension extension = getTarget(IProgramExtension.class);
			if (extension != null) {
				extension.removeInterface(classes);
			}
		} finally {
			ungetTarget();
		}
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
	public <T> void adapt(Class<T>[] classes, T object) {
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				programExtension.adapt(classes, object);
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

	@Override
	public int hashCode() {
		int hashCode = 0;
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				hashCode = programExtension.hashCode();
			}
		} finally {
			ungetTarget();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		boolean equals = false;
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				equals = programExtension.equals(obj);
			}
		} finally {
			ungetTarget();
		}
		return equals;
	}

	@Override
	public String toString() {
		String string = null;
		try {
			IProgramExtension programExtension = getTarget(IProgramExtension.class);
			if (programExtension != null) {
				string = programExtension.toString();
			}
		} finally {
			ungetTarget();
		}
		return string;
	}

}

// @Override
// public IProgramLauncher getLauncher() {
// IProgramLauncher launcher = null;
// try {
// IProgramExtension programExtension = getTarget(IProgramExtension.class);
// if (programExtension != null) {
// launcher = programExtension.getLauncher();
// }
// } finally {
// ungetTarget();
// }
// return launcher;
// }

// @Override
// public String[] getParameters() {
// String[] parameters = null;
// try {
// IProgramExtension programExtension = getTarget(IProgramExtension.class);
// if (programExtension != null) {
// parameters = programExtension.getParameters();
// }
// } finally {
// ungetTarget();
// }
// return parameters;
// }

// @Override
// public <T> void addInterface(Class<T>[] classes, T object, String name) {
// try {
// IProgramExtension extension = getTarget(IProgramExtension.class);
// if (extension != null) {
// extension.addInterface(classes, object, name);
// }
// } finally {
// ungetTarget();
// }
// }

// @Override
// public <T> void addInterface(Class<T> clazz, T object, String name) {
// try {
// IProgramExtension extension = getTarget(IProgramExtension.class);
// if (extension != null) {
// extension.addInterface(clazz, object, name);
// }
// } finally {
// ungetTarget();
// }
// }

// @Override
// public <T> void addInterface(Class<?>[] classes, T object) {
// try {
// IProgramExtension extension = getTarget(IProgramExtension.class);
// if (extension != null) {
// extension.addInterface(classes, object);
// }
// } finally {
// ungetTarget();
// }
// }

// @Override
// public <T> void addInterface(Class<?>[] classes, T object, InterfaceDescription description) {
// try {
// IProgramExtension extension = getTarget(IProgramExtension.class);
// if (extension != null) {
// extension.addInterface(classes, object, description);
// }
// } finally {
// ungetTarget();
// }
// }
