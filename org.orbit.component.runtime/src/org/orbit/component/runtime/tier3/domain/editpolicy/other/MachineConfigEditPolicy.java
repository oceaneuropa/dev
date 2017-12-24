package org.orbit.component.runtime.tier3.domain.editpolicy.other;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.domain.command.other.MachineConfigAddCommand;
import org.orbit.component.runtime.tier3.domain.command.other.MachineConfigGetCommand;
import org.orbit.component.runtime.tier3.domain.command.other.MachineConfigRemoveCommand;
import org.orbit.component.runtime.tier3.domain.command.other.MachineConfigUpdateCommand;
import org.orbit.component.runtime.tier3.domain.command.other.MachineConfigsGetCommand;
import org.orbit.component.runtime.tier3.domain.service.DomainService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class MachineConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		DomainService service = resource.getService(DomainService.class);

		String requestName = request.getRequestName();

		if (Requests.GET_MACHINE_CONFIGS.equals(requestName)) {
			return new MachineConfigsGetCommand(service, request);

		} else if (Requests.GET_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigGetCommand(service, request);

		} else if (Requests.ADD_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigAddCommand(service, request);

		} else if (Requests.UPDATE_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigUpdateCommand(service, request);

		} else if (Requests.REMOVE_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigRemoveCommand(service, request);
		}

		return null;
	}

}
