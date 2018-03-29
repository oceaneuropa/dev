package org.origin.common.extensions.util;

import org.origin.common.extensions.InterfaceDescription;

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
