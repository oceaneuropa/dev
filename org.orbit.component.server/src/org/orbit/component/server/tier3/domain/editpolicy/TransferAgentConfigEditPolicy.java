package org.orbit.component.server.tier3.domain.editpolicy;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.domain.command.TransferAgentConfigAddCommand;
import org.orbit.component.server.tier3.domain.command.TransferAgentConfigGetCommand;
import org.orbit.component.server.tier3.domain.command.TransferAgentConfigRemoveCommand;
import org.orbit.component.server.tier3.domain.command.TransferAgentConfigUpdateCommand;
import org.orbit.component.server.tier3.domain.command.TransferAgentConfigsGetCommand;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.agent.AbstractWSEditPolicy;
import org.origin.common.rest.model.Request;

public class TransferAgentConfigEditPolicy extends AbstractWSEditPolicy {

	@Override
	public ICommand getCommand(Request request) {
		DomainManagementService service = super.getService(DomainManagementService.class);
		if (service == null) {
			return null;
		}

		String requestName = request.getRequestName();

		if (OrbitConstants.Requests.GET_TA_CONFIGS.equals(requestName)) {
			return new TransferAgentConfigsGetCommand(service, request);

		} else if (OrbitConstants.Requests.GET_TA_CONFIG.equals(requestName)) {
			return new TransferAgentConfigGetCommand(service, request);

		} else if (OrbitConstants.Requests.ADD_TA_CONFIG.equals(requestName)) {
			return new TransferAgentConfigAddCommand(service, request);

		} else if (OrbitConstants.Requests.UPDATE_TA_CONFIG.equals(requestName)) {
			return new TransferAgentConfigUpdateCommand(service, request);

		} else if (OrbitConstants.Requests.REMOVE_TA_CONFIG.equals(requestName)) {
			return new TransferAgentConfigRemoveCommand(service, request);
		}

		return null;
	}

}
