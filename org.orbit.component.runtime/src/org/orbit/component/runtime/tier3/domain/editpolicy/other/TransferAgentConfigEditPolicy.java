package org.orbit.component.runtime.tier3.domain.editpolicy.other;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.domain.command.other.TransferAgentConfigAddCommand;
import org.orbit.component.runtime.tier3.domain.command.other.TransferAgentConfigGetCommand;
import org.orbit.component.runtime.tier3.domain.command.other.TransferAgentConfigRemoveCommand;
import org.orbit.component.runtime.tier3.domain.command.other.TransferAgentConfigUpdateCommand;
import org.orbit.component.runtime.tier3.domain.command.other.TransferAgentConfigsGetCommand;
import org.orbit.component.runtime.tier3.domain.service.DomainService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class TransferAgentConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		DomainService service = resource.getService(DomainService.class);

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