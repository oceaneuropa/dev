package org.orbit.component.connector.tier3.domain;

import org.orbit.component.api.tier3.domain.MachineConfig;

public class MachineConfigImpl implements MachineConfig {

	protected String id;
	protected String name;
	protected String ipAddress;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString() {
		return "MachineConfigImpl [id=" + id + ", name=" + name + ", ipAddress=" + ipAddress + "]";
	}

}
