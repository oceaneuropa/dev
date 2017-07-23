package org.orbit.component.model.tier3.domain.request;

import org.origin.common.rest.model.Request;

public class AddMachineConfigRequest extends Request {

	public static final String REQUEST_NAME = "add_machine_config";

	protected String machineId;
	protected String name;
	protected String ipAddress;

	public AddMachineConfigRequest() {
		super(REQUEST_NAME);
	}

	public String getMachineId() {
		return this.machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
		setParameter("machineId", machineId);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		setParameter("name", name);
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		setParameter("ipAddress", ipAddress);
	}

}
