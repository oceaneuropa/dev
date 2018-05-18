package org.origin.common.extensions.util;

import org.origin.common.extensions.InterfaceDescription;

public interface InterfacesAware {

	Object[] getInterfaces();

	InterfaceDescription[] getInterfaceDescriptions();

	InterfaceDescription getInterfaceDescription(Class<?> clazz);

	InterfaceDescription getInterfaceDescription(Object object);

	<T> T getInterface(Class<T> clazz);

	<T> void addInterface(Class<?> clazz, T interfaceInstance);

	void addInterface(Class<?> clazz, String interfaceClassName);

	void addInterface(Class<?> clazz, Class<?> interfaceImplClass);

	void addInterface(InterfaceDescription description);

	void removeInterface(Class<?>... classes);

}
