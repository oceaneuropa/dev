package org.orbit.service.program.impl;

import java.util.Map;

import org.orbit.service.program.IProgramExtensionFilter;
import org.orbit.service.program.IProgramLauncher;
import org.orbit.service.program.util.ProgramExtension;

public class ProgramExtensionDescriptiveImpl extends ProgramExtensionImpl {

	protected ProgramExtension programExtensionModel;

	/**
	 * 
	 * @param programExtensionModel
	 */
	public ProgramExtensionDescriptiveImpl(ProgramExtension programExtensionModel) {
		this.programExtensionModel = programExtensionModel;
	}

	@Override
	public String getTypeId() {
		return this.programExtensionModel.getTypeId();
	}

	@Override
	public String getId() {
		return this.programExtensionModel.getId();
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.programExtensionModel.getProperties();
	}

	@Override
	public IProgramLauncher getLauncher() {
		return this.programExtensionModel.getLauncher();
	}

	@Override
	public IProgramExtensionFilter getFilter() {
		return this.programExtensionModel.getFilter();
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.programExtensionModel.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.programExtensionModel.getAdapter(adapter);
	}

}
