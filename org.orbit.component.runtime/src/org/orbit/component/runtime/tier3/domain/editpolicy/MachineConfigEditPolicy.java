package org.orbit.component.runtime.tier3.domain.editpolicy;

import org.orbit.component.runtime.OrbitConstants;
import org.orbit.component.runtime.tier3.domain.command.MachineConfigAddCommand;
import org.orbit.component.runtime.tier3.domain.command.MachineConfigGetCommand;
import org.orbit.component.runtime.tier3.domain.command.MachineConfigRemoveCommand;
import org.orbit.component.runtime.tier3.domain.command.MachineConfigUpdateCommand;
import org.orbit.component.runtime.tier3.domain.command.MachineConfigsGetCommand;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class MachineConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		// DomainManagementService service = super.getService(DomainManagementService.class);
		// if (this.service == null) {
		// return null;
		// }
		DomainManagementService service = resource.getService(DomainManagementService.class);

		String requestName = request.getRequestName();

		if (OrbitConstants.Requests.GET_MACHINE_CONFIGS.equals(requestName)) {
			return new MachineConfigsGetCommand(service, request);

		} else if (OrbitConstants.Requests.GET_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigGetCommand(service, request);

		} else if (OrbitConstants.Requests.ADD_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigAddCommand(service, request);

		} else if (OrbitConstants.Requests.UPDATE_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigUpdateCommand(service, request);

		} else if (OrbitConstants.Requests.REMOVE_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigRemoveCommand(service, request);
		}

		return null;
	}

}
