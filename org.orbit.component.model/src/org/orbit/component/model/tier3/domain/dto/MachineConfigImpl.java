package org.orbit.component.model.tier3.domain.dto;

public class MachineConfigImpl implements MachineConfig {

	protected String id;
	protected String name;
	protected String ipAddress;

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString() {
		return "MachineConfigImpl [id=" + id + ", name=" + name + ", ipAddress=" + ipAddress + "]";
	}

}
