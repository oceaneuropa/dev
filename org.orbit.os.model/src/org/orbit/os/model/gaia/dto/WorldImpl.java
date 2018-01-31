package org.orbit.os.model.gaia.dto;

public class WorldImpl implements World {

	protected String name;

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
