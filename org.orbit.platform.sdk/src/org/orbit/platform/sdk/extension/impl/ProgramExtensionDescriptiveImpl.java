package org.orbit.platform.sdk.extension.impl;

import java.util.Map;

import org.orbit.platform.sdk.extension.IProgramExtensionFilter;
import org.orbit.platform.sdk.extension.util.ProgramExtension;

public class ProgramExtensionDescriptiveImpl extends ProgramExtensionImpl {

	protected ProgramExtension programExtensionDesc;

	/**
	 * 
	 * @param programExtensionDesc
	 */
	public ProgramExtensionDescriptiveImpl(ProgramExtension programExtensionDesc) {
		this.programExtensionDesc = programExtensionDesc;
	}

	@Override
	public String getTypeId() {
		return this.programExtensionDesc.getTypeId();
	}

	@Override
	public String getId() {
		return this.programExtensionDesc.getId();
	}

	@Override
	public String getName() {
		return this.programExtensionDesc.getName();
	}

	@Override
	public String getDescription() {
		return this.programExtensionDesc.getDescription();
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.programExtensionDesc.getProperties();
	}

	@Override
	public IProgramExtensionFilter getFilter() {
		return this.programExtensionDesc.getFilter();
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.programExtensionDesc.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.programExtensionDesc.getAdapter(adapter);
	}

}

// @Override
// public IProgramLauncher getLauncher() {
// return this.programExtensionModel.getLauncher();
// }
