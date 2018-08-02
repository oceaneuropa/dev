package org.orbit.component.model.tier3.domain;

import org.origin.common.rest.model.Request;

public class RemoveMachineConfigRequest extends Request {

	public static final String REQUEST_NAME = "remove_machine_config";

	protected String machineId;

	public RemoveMachineConfigRequest() {
		super(REQUEST_NAME);
	}

	public String getMachineId() {
		return this.machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
		setParameter("machineId", machineId);
	}

	public static RemoveMachineConfigRequest parse(Request request) {
		RemoveMachineConfigRequest newRequest = null;
		if (REQUEST_NAME.equals(request.getRequestName())) {
			String machineId = (String) request.getParameter("machineId");

			newRequest = new RemoveMachineConfigRequest();
			newRequest.setMachineId(machineId);
		}
		return newRequest;
	}

}
