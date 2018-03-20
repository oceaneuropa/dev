package org.orbit.platform.sdk.extension;

import java.util.HashMap;
import java.util.Map;

import org.orbit.platform.sdk.extension.desc.InterfaceDescription;

public class InterfacesSupport implements InterfacesAware {

	protected Map<Class<?>, Object> classToInterfaceMap = new HashMap<Class<?>, Object>();
	protected Map<Class<?>, Object> classToInstanceMap = new HashMap<Class<?>, Object>();
	protected Map<Object, InterfaceDescription> interfaceToDescriptionMap = new HashMap<Object, InterfaceDescription>();

	@Override
	public Object[] getInterfaces() {
		return this.classToInterfaceMap.values().toArray(new Object[this.classToInterfaceMap.size()]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T getInterface(Class<T> clazz) {
		T t = null;
		if (clazz != null) {
			Object object = this.classToInterfaceMap.get(clazz);

			if (object != null) {
				if (object instanceof String) {
					Object instance = this.classToInstanceMap.get(clazz);

					if (instance != null && clazz.isAssignableFrom(instance.getClass())) {
						t = (T) instance;

					} else {
						try {
							Class<?> implClass = Class.forName((String) object);
							if (implClass != null) {
								instance = implClass.newInstance();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (instance != null && clazz.isAssignableFrom(instance.getClass())) {
							this.classToInstanceMap.put(clazz, instance);
							t = (T) instance;
						}
					}

				} else if (clazz.isAssignableFrom(object.getClass())) {
					t = (T) object;
				}
			}
		}
		return t;
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Class<?> clazz) {
		InterfaceDescription desc = null;
		if (clazz != null) {
			Object object = getInterface(clazz);
			if (object != null) {
				desc = getInterfaceDescription((Object) object);
			}
		}
		return desc;
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Object object) {
		InterfaceDescription desc = null;
		if (object != null) {
			desc = this.interfaceToDescriptionMap.get(object);
		}
		return desc;
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T object) {
		if (clazz != null && object != null) {
			addInterface(clazz, object, new InterfaceDescription(clazz.getSimpleName()));
		}
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T object, InterfaceDescription description) {
		if (clazz != null && object != null) {
			this.classToInterfaceMap.put(clazz, object);

			if (description != null) {
				this.interfaceToDescriptionMap.put(object, description);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void addInterface(Class<?>[] classes, T object) {
		if (classes != null && object != null) {
			for (Class<?> clazz : classes) {
				addInterface((Class<T>) clazz, object);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void addInterface(Class<?>[] classes, T object, InterfaceDescription description) {
		if (classes != null && object != null) {
			for (Class<?> clazz : classes) {
				addInterface((Class<T>) clazz, object, description);
			}
		}
	}

	@Override
	public void removeInterface(Class<?>... classes) {
		if (classes != null) {
			for (Class<?> clazz : classes) {
				this.classToInterfaceMap.remove(clazz);
			}
		}
	}

}

// @Override
// public <T> void addInterface(Class<T>[] classes, T object, String name) {
// if (classes != null && object != null) {
// for (Class<T> clazz : classes) {
// addInterface((Class<T>) clazz, object, name);
// }
// }
// }
