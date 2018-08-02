package org.orbit.spirit.connector.gaia;

import org.orbit.spirit.api.gaia.World;

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
