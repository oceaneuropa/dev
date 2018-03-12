package org.orbit.platform.sdk.extension;

import org.orbit.platform.sdk.extension.desc.InterfaceDescription;

public interface InterfacesAware {

	Object[] getInterfaces();

	<T> T getInterface(Class<T> clazz);

	InterfaceDescription getInterfaceDescription(Class<?> clazz);

	InterfaceDescription getInterfaceDescription(Object object);

	<T> void addInterface(Class<T> clazz, T object);

	<T> void addInterface(Class<T> clazz, T object, String name);

	<T> void addInterface(Class<T> clazz, T object, InterfaceDescription description);

	<T> void addInterface(Class<T>[] classes, T object);

	<T> void addInterface(Class<T>[] classes, T object, String name);

	<T> void addInterface(Class<T>[] classes, T object, InterfaceDescription description);

	void removeInterface(Class<?>... classes);

}
