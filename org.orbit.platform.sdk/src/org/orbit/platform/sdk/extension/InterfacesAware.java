package org.orbit.platform.sdk.extension;

import org.orbit.platform.sdk.extension.desc.InterfaceDescription;

public interface InterfacesAware {

	Object[] getInterfaces();

	<T> T getInterface(Class<T> clazz);

	InterfaceDescription getInterfaceDescription(Class<?> clazz);

	InterfaceDescription getInterfaceDescription(Object object);

	<T> void addInterface(Class<?> clazz, T interfaceInstance);

	void addInterface(Class<?> clazz, String interfaceClassName);

	void addInterface(Class<?> clazz, Class<?> interfaceImplClass);

	void addInterface(InterfaceDescription description);

	void removeInterface(Class<?>... classes);

}

// <T> void addInterface(Class<?>[] classes, T object);

// <T> void addInterface(Class<?>[] classes, T object, InterfaceDescription description);
