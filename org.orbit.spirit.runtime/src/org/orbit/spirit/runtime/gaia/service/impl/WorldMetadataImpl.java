package org.orbit.spirit.runtime.gaia.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.orbit.spirit.runtime.gaia.service.WorldMetadata;

public class WorldMetadataImpl implements WorldMetadata {

	protected String name;
	protected Map<String, String> status;

	public WorldMetadataImpl() {
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
