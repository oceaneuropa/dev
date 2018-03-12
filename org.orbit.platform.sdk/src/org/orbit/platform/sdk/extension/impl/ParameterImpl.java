package org.orbit.platform.sdk.extension.impl;

import org.orbit.platform.sdk.extension.IParameter;

public class ParameterImpl implements IParameter {

	protected String name;
	protected String description;

	public ParameterImpl() {
	}

	public ParameterImpl(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
