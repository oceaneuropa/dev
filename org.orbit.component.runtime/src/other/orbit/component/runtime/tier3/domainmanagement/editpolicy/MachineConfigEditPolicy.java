package other.orbit.component.runtime.tier3.domainmanagement.editpolicy;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.other.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.other.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

import other.orbit.component.runtime.tier3.domainmanagement.command.MachineConfigAddCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.MachineConfigGetCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.MachineConfigRemoveCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.MachineConfigUpdateCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.MachineConfigsGetCommand;

public class MachineConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		DomainManagementService service = resource.getService(DomainManagementService.class);

		String requestName = request.getRequestName();

		if (RequestConstants.GET_MACHINE_CONFIGS.equals(requestName)) {
			return new MachineConfigsGetCommand(service, request);

		} else if (RequestConstants.GET_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigGetCommand(service, request);

		} else if (RequestConstants.ADD_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigAddCommand(service, request);

		} else if (RequestConstants.UPDATE_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigUpdateCommand(service, request);

		} else if (RequestConstants.REMOVE_MACHINE_CONFIG.equals(requestName)) {
			return new MachineConfigRemoveCommand(service, request);
		}

		return null;
	}

}
