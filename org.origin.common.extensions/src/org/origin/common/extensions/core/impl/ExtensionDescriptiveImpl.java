package org.origin.common.extensions.core.impl;

import java.util.Map;

import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.core.IExtension;

public class ExtensionDescriptiveImpl extends ExtensionImpl implements IExtension {

	protected String realm;
	protected Extension extension;

	/**
	 * 
	 * @param realm
	 * @param extension
	 */
	public ExtensionDescriptiveImpl(String realm, Extension extension) {
		this.realm = realm;
		this.extension = extension;
	}

	@Override
	public String getRealm() {
		return this.realm;
	}

	@Override
	public String getTypeId() {
		return this.extension.getTypeId();
	}

	@Override
	public String getId() {
		return this.extension.getId();
	}

	@Override
	public String getName() {
		return this.extension.getName();
	}

	@Override
	public String getDescription() {
		return this.extension.getDescription();
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.extension.getProperties();
	}

	@Override
	public Object[] getInterfaces() {
		return this.extension.getInterfaces();
	}

	@Override
	public <T> T getInterface(Class<T> clazz) {
		return this.extension.getInterface(clazz);
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Class<?> clazz) {
		return this.extension.getInterfaceDescription(clazz);
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Object object) {
		return this.extension.getInterfaceDescription(object);
	}

	@Override
	public void addInterface(InterfaceDescription description) {
		this.extension.addInterface(description);
	}

	@Override
	public void addInterface(Class<?> clazz, String interfaceClassName) {
		this.extension.addInterface(clazz, interfaceClassName);
	}

	@Override
	public void addInterface(Class<?> clazz, Class<?> interfaceImplClass) {
		this.extension.addInterface(clazz, interfaceImplClass);
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T object) {
		this.extension.addInterface(clazz, object);
	}

	@Override
	public void removeInterface(Class<?>... classes) {
		this.extension.removeInterface(classes);
	}

}
