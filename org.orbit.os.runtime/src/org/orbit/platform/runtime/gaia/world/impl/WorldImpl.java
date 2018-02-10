package org.orbit.platform.runtime.gaia.world.impl;

import java.util.HashMap;
import java.util.Map;

import org.orbit.platform.runtime.gaia.world.World;

public class WorldImpl implements World {

	protected String name;
	protected Map<String, String> status;

	public WorldImpl() {
		this.status = new HashMap<String, String>();
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
	public Map<String, String> getStatus() {
		return this.status;
	}

	@Override
	public void setStatus(Map<String, String> status) {
		if (status != null && !status.isEmpty()) {
			this.status.putAll(status);
		}
	}

	@Override
	public void setStatus(String key, String value) {
		if (key != null) {
			if (value != null) {
				this.status.put(key, value);
			} else {
				this.status.remove(key);
			}
		}
	}

}
