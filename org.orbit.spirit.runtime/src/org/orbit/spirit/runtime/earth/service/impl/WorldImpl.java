package org.orbit.spirit.runtime.earth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.spirit.runtime.earth.service.World;

public class WorldImpl implements World {

	protected String gaiaId;
	protected String earthId;
	protected String id;
	protected String name;
	protected List<String> accountIds = new ArrayList<String>();

	@Override
	public String getGaiaId() {
		return this.gaiaId;
	}

	@Override
	public void setGaiaId(String gaiaId) {
		this.gaiaId = gaiaId;
	}

	@Override
	public String getEarthId() {
		return this.earthId;
	}

	@Override
	public void setEarthId(String earthId) {
		this.earthId = earthId;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<String> getAccountIds() {
		return this.accountIds;
	}

}
