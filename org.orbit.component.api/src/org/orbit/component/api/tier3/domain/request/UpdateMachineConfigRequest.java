package org.orbit.component.api.tier3.domain.request;

import java.util.ArrayList;
import java.util.List;

public class UpdateMachineConfigRequest {

	protected String machineId;
	protected String name;
	protected String ipAddress;
	protected List<String> fieldsToUpdate = new ArrayList<String>();

	public String getMachineId() {
		return this.machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		this.fieldsToUpdate.add("name");
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		this.fieldsToUpdate.add("ipAddress");
	}

	public List<String> getFieldsToUpdate() {
		return this.fieldsToUpdate;
	}

	public void setFieldsToUpdate(List<String> fieldsToUpdate) {
		this.fieldsToUpdate = fieldsToUpdate;
	}

}
