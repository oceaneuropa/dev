package org.orbit.component.runtime.tier3.domain.editpolicy;

import org.orbit.component.runtime.Requests;
import org.orbit.component.runtime.tier3.domain.command.TransferAgentConfigAddCommand;
import org.orbit.component.runtime.tier3.domain.command.TransferAgentConfigGetCommand;
import org.orbit.component.runtime.tier3.domain.command.TransferAgentConfigRemoveCommand;
import org.orbit.component.runtime.tier3.domain.command.TransferAgentConfigUpdateCommand;
import org.orbit.component.runtime.tier3.domain.command.TransferAgentConfigsGetCommand;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class TransferAgentConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		// DomainManagementService service = super.getService(DomainManagementService.class);
		// if (this.service == null) {
		// return null;
		// }
		DomainManagementService service = resource.getService(DomainManagementService.class);

		String requestName = request.getRequestName();

		if (Requests.GET_TA_CONFIGS.equals(requestName)) {
			return new TransferAgentConfigsGetCommand(service, request);

		} else if (Requests.GET_TA_CONFIG.equals(requestName)) {
			return new TransferAgentConfigGetCommand(service, request);

		} else if (Requests.ADD_TA_CONFIG.equals(requestName)) {
			return new TransferAgentConfigAddCommand(service, request);

		} else if (Requests.UPDATE_TA_CONFIG.equals(requestName)) {
			return new TransferAgentConfigUpdateCommand(service, request);

		} else if (Requests.REMOVE_TA_CONFIG.equals(requestName)) {
			return new TransferAgentConfigRemoveCommand(service, request);
		}

		return null;
	}

}
