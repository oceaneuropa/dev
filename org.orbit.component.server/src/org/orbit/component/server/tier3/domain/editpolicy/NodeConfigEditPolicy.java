package org.orbit.component.server.tier3.domain.editpolicy;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.domain.command.NodeConfigAddCommand;
import org.orbit.component.server.tier3.domain.command.NodeConfigGetCommand;
import org.orbit.component.server.tier3.domain.command.NodeConfigRemoveCommand;
import org.orbit.component.server.tier3.domain.command.NodeConfigUpdateCommand;
import org.orbit.component.server.tier3.domain.command.NodeConfigsGetCommand;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class NodeConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		// DomainManagementService service = super.getService(DomainManagementService.class);
		// if (service == null) {
		// return null;
		// }
		DomainManagementService service = resource.getService(DomainManagementService.class);

		String requestName = request.getRequestName();

		if (OrbitConstants.Requests.GET_NODE_CONFIGS.equals(requestName)) {
			return new NodeConfigsGetCommand(service, request);

		} else if (OrbitConstants.Requests.GET_NODE_CONFIG.equals(requestName)) {
			return new NodeConfigGetCommand(service, request);

		} else if (OrbitConstants.Requests.ADD_NODE_CONFIG.equals(requestName)) {
			return new NodeConfigAddCommand(service, request);

		} else if (OrbitConstants.Requests.UPDATE_NODE_CONFIG.equals(requestName)) {
			return new NodeConfigUpdateCommand(service, request);

		} else if (OrbitConstants.Requests.REMOVE_NODE_CONFIG.equals(requestName)) {
			return new NodeConfigRemoveCommand(service, request);
		}

		return null;
	}

}
