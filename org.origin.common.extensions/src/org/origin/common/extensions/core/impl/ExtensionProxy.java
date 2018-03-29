package org.origin.common.extensions.core.impl;

import java.util.Map;

import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.core.IExtension;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ExtensionProxy implements IExtension {

	protected BundleContext context;
	protected ServiceReference<?> reference;
	protected String realm;
	protected String typeId;
	protected String id;

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param realm
	 * @param typeId
	 *            extension type id. e.g. "orbit.switcher"
	 * @param id
	 *            extension id. e.g. "UserRegistrySwitcher", "AuthSwitcher", "AppStoreSwitcher", etc.
	 */
	public ExtensionProxy(BundleContext context, ServiceReference<?> reference, String realm, String typeId, String id) {
		this.context = context;
		this.reference = reference;
		this.realm = realm;
		this.typeId = typeId;
		this.id = id;
	}

	@Override
	public String getRealm() {
		return this.realm;
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
			IExtension extension = getTarget(IExtension.class);
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
			IExtension extension = getTarget(IExtension.class);
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
			IExtension extension = getTarget(IExtension.class);
			if (extension != null) {
				properties = extension.getProperties();
			}
		} finally {
			ungetTarget();
		}
		return properties;
	}

	@Override
	public Object[] getInterfaces() {
		Object[] interfaces = null;
		try {
			IExtension extension = getTarget(IExtension.class);
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
			IExtension extension = getTarget(IExtension.class);
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
			IExtension extension = getTarget(IExtension.class);
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
			IExtension extension = getTarget(IExtension.class);
			if (extension != null) {
				desc = extension.getInterfaceDescription(object);
			}
		} finally {
			ungetTarget();
		}
		return desc;
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T interfaceInstance) {
		try {
			IExtension extension = getTarget(IExtension.class);
			if (extension != null) {
				extension.addInterface(clazz, interfaceInstance);
			}
		} finally {
			ungetTarget();
		}
	}

	@Override
	public void addInterface(Class<?> clazz, String interfaceClassName) {
		try {
			IExtension extension = getTarget(IExtension.class);
			if (extension != null) {
				extension.addInterface(clazz, interfaceClassName);
			}
		} finally {
			ungetTarget();
		}
	}

	@Override
	public void addInterface(Class<?> clazz, Class<?> interfaceImplClass) {
		try {
			IExtension extension = getTarget(IExtension.class);
			if (extension != null) {
				extension.addInterface(clazz, interfaceImplClass);
			}
		} finally {
			ungetTarget();
		}
	}

	@Override
	public void addInterface(InterfaceDescription description) {
		try {
			IExtension extension = getTarget(IExtension.class);
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
			IExtension extension = getTarget(IExtension.class);
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
			IExtension programExtension = getTarget(IExtension.class);
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
			IExtension programExtension = getTarget(IExtension.class);
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
			IExtension programExtension = getTarget(IExtension.class);
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
			IExtension programExtension = getTarget(IExtension.class);
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
			IExtension programExtension = getTarget(IExtension.class);
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
			IExtension programExtension = getTarget(IExtension.class);
			if (programExtension != null) {
				string = programExtension.toString();
			}
		} finally {
			ungetTarget();
		}
		return string;
	}

}
