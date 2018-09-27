package org.orbit.spirit.runtime.earth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.spirit.runtime.earth.service.EarthService;
import org.orbit.spirit.runtime.earth.service.World;

public class WorldImpl implements World {

	protected EarthService earth;
	protected String id;
	protected String name;
	protected List<String> accountIds = new ArrayList<String>();

	/**
	 * 
	 * @param earth
	 */
	public WorldImpl(EarthService earth) {
		this.earth = earth;
	}

	@Override
	public String getGaiaId() {
		return this.earth.getGaiaId();
	}

	@Override
	public String getEarthId() {
		return this.earth.getEarthId();
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

	@Override
	public void dispose() {

	}

}
