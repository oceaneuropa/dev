package org.orbit.platform.sdk.extension.impl;

import java.util.Map;

import org.orbit.platform.sdk.extension.IProgramExtensionFilter;
import org.orbit.platform.sdk.extension.desc.InterfaceDescription;
import org.orbit.platform.sdk.extension.desc.ProgramExtension;

public class ProgramExtensionDescriptiveImpl extends ProgramExtensionImpl {

	protected ProgramExtension extensionDesc;

	/**
	 * 
	 * @param extensionDesc
	 */
	public ProgramExtensionDescriptiveImpl(ProgramExtension extensionDesc) {
		this.extensionDesc = extensionDesc;
	}

	@Override
	public String getTypeId() {
		return this.extensionDesc.getTypeId();
	}

	@Override
	public String getId() {
		return this.extensionDesc.getId();
	}

	@Override
	public String getName() {
		return this.extensionDesc.getName();
	}

	@Override
	public String getDescription() {
		return this.extensionDesc.getDescription();
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.extensionDesc.getProperties();
	}

	@Override
	public IProgramExtensionFilter getFilter() {
		return this.extensionDesc.getFilter();
	}

	@Override
	public Object[] getInterfaces() {
		return this.extensionDesc.getInterfaces();
	}

	@Override
	public <T> T getInterface(Class<T> clazz) {
		return this.extensionDesc.getInterface(clazz);
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Class<?> clazz) {
		return this.extensionDesc.getInterfaceDescription(clazz);
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Object object) {
		return this.extensionDesc.getInterfaceDescription(object);
	}

	@Override
	public <T> void addInterface(InterfaceDescription description) {
		this.extensionDesc.addInterface(description);
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T object) {
		this.extensionDesc.addInterface(clazz, object);
	}

	@Override
	public void removeInterface(Class<?>... classes) {
		this.extensionDesc.removeInterface(classes);
	}

}

// @Override
// public IProgramLauncher getLauncher() {
// return this.programExtensionModel.getLauncher();
// }

// @Override
// public String[] getParameters() {
// return this.programExtensionDesc.getParameters();
// }

// @Override
// public <T> void adapt(Class<T> clazz, T object) {
// this.programExtensionDesc.adapt(clazz, object);
// }
//
// @Override
// public <T> void adapt(Class<T>[] classes, T object) {
// this.programExtensionDesc.adapt(classes, object);
// }
//
// @Override
// public <T> T getAdapter(Class<T> adapter) {
// return this.programExtensionDesc.getAdapter(adapter);
// }

// @Override
// public <T> void addInterface(Class<T> clazz, T object, String name) {
// this.extensionDesc.addInterface(clazz, object, name);
// }
//
// @Override
// public <T> void addInterface(Class<T>[] classes, T object, String name) {
// this.extensionDesc.addInterface(classes, object, name);
// }

// @Override
// public <T> void addInterface(Class<?> clazz, T object, InterfaceDescription description) {
// this.extensionDesc.addInterface(clazz, object, description);
// }
//
// @Override
// public <T> void addInterface(Class<?>[] classes, T object) {
// this.extensionDesc.addInterface(classes, object);
// }
//
// @Override
// public <T> void addInterface(Class<?>[] classes, T object, InterfaceDescription description) {
// this.extensionDesc.addInterface(classes, object, description);
// }
