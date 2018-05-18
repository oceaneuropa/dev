package org.origin.common.extensions.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.origin.common.extensions.InterfaceDescription;

public class InterfacesSupport implements InterfacesAware {

	protected Map<Class<?>, InterfaceDescription> classToDescriptionMap = new HashMap<Class<?>, InterfaceDescription>();
	protected Map<Class<?>, Object> classToInstanceMap = new HashMap<Class<?>, Object>();

	@Override
	public Object[] getInterfaces() {
		List<Object> interfaces = new ArrayList<Object>();
		for (Iterator<Class<?>> itor = this.classToDescriptionMap.keySet().iterator(); itor.hasNext();) {
			Class<?> interfaceClass = itor.next();
			Object object = getInterface(interfaceClass);
			if (object != null) {
				interfaces.add(object);
			}
		}
		return interfaces.toArray(new Object[interfaces.size()]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T getInterface(Class<T> clazz) {
		T t = null;
		if (clazz != null) {
			InterfaceDescription desc = this.classToDescriptionMap.get(clazz);

			if (desc != null) {
				Object object = desc.getInterfaceInstance();
				if (object != null && clazz.isAssignableFrom(object.getClass())) {
					t = (T) object;
				}
				if (t == null) {
					Object instance = this.classToInstanceMap.get(clazz);
					if (instance != null && clazz.isAssignableFrom(instance.getClass())) {
						t = (T) instance;

					} else {
						Class<?> implClass = desc.getInterfaceImplClass();
						if (implClass == null) {
							String className = desc.getInterfaceClassName();
							if (className != null) {
								ClassLoader cl = Thread.currentThread().getContextClassLoader();
								try {
									ClassLoader extensionClassLoader = getClass().getClassLoader();
									Thread.currentThread().setContextClassLoader(extensionClassLoader);

									try {
										implClass = Class.forName((String) className);
									} catch (Exception e) {
										e.printStackTrace();
									}

								} finally {
									Thread.currentThread().setContextClassLoader(cl);
								}
							}
						}

						if (implClass != null) {
							try {
								instance = implClass.newInstance();
								if (instance != null && clazz.isAssignableFrom(instance.getClass())) {
									this.classToInstanceMap.put(clazz, instance);
									t = (T) instance;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return t;
	}

	@Override
	public InterfaceDescription[] getInterfaceDescriptions() {
		return this.classToDescriptionMap.values().toArray(new InterfaceDescription[this.classToDescriptionMap.size()]);
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Class<?> clazz) {
		InterfaceDescription desc = null;
		if (clazz != null) {
			desc = this.classToDescriptionMap.get(clazz);
		}
		return desc;
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Object object) {
		InterfaceDescription desc = null;
		if (object != null) {
			for (Iterator<Class<?>> itor = this.classToDescriptionMap.keySet().iterator(); itor.hasNext();) {
				Class<?> interfaceClass = itor.next();
				InterfaceDescription currDesc = this.classToDescriptionMap.get(interfaceClass);
				if (currDesc != null) {
					Object interfaceObject = currDesc.getInterfaceInstance();
					if (object.equals(interfaceObject)) {
						desc = currDesc;
						break;
					}
				}
			}

			if (desc == null) {
				for (Iterator<Class<?>> itor = this.classToInstanceMap.keySet().iterator(); itor.hasNext();) {
					Class<?> interfaceClass = itor.next();
					Object interfaceInstance = this.classToInstanceMap.get(interfaceClass);
					if (object.equals(interfaceInstance)) {
						InterfaceDescription currDesc = this.classToDescriptionMap.get(interfaceClass);
						if (currDesc != null) {
							desc = currDesc;
							break;
						}
					}
				}
			}
		}
		return desc;
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T interfaceInstance) {
		if (clazz != null) {
			InterfaceDescription desc = new InterfaceDescription(clazz.getSimpleName());
			desc.setInterfaceClass(clazz);
			desc.setInterfaceInstance(interfaceInstance);
			addInterface(desc);
		}
	}

	@Override
	public void addInterface(Class<?> clazz, String interfaceClassName) {
		if (clazz != null) {
			InterfaceDescription desc = new InterfaceDescription(clazz.getSimpleName());
			desc.setInterfaceClass(clazz);
			desc.setInterfaceClassName(interfaceClassName);
			addInterface(desc);
		}
	}

	@Override
	public void addInterface(Class<?> clazz, Class<?> interfaceImplClass) {
		if (clazz != null) {
			InterfaceDescription desc = new InterfaceDescription(clazz.getSimpleName());
			desc.setInterfaceClass(clazz);
			desc.setInterfaceImplClass(interfaceImplClass);
			addInterface(desc);
		}
	}

	@Override
	public void addInterface(InterfaceDescription desc) {
		if (desc == null) {
			throw new IllegalArgumentException("InterfaceDescription is null.");
		}
		Class<?> interfaceClass = desc.getInterfaceClass();
		if (interfaceClass == null) {
			throw new IllegalArgumentException("interfaceClass is null.");
		}
		this.classToDescriptionMap.put(interfaceClass, desc);
	}

	@Override
	public void removeInterface(Class<?>... classes) {
		if (classes != null) {
			for (Class<?> clazz : classes) {
				this.classToDescriptionMap.remove(clazz);
				this.classToInstanceMap.remove(clazz);
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

// @SuppressWarnings("unchecked")
// @Override
// public <T> void addInterface(Class<?>[] classes, T object) {
// if (classes != null && object != null) {
// for (Class<?> clazz : classes) {
// addInterface((Class<T>) clazz, object);
// }
// }
// }

// @SuppressWarnings("unchecked")
// @Override
// public <T> void addInterface(Class<?>[] classes, T object, InterfaceDescription description) {
// if (classes != null && object != null) {
// for (Class<?> clazz : classes) {
// addInterface((Class<T>) clazz, object, description);
// }
// }
// }
