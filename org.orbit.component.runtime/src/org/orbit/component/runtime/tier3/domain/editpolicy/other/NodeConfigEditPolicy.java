package org.orbit.component.runtime.tier3.domain.editpolicy.other;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.domain.command.other.NodeConfigAddCommand;
import org.orbit.component.runtime.tier3.domain.command.other.NodeConfigGetCommand;
import org.orbit.component.runtime.tier3.domain.command.other.NodeConfigRemoveCommand;
import org.orbit.component.runtime.tier3.domain.command.other.NodeConfigUpdateCommand;
import org.orbit.component.runtime.tier3.domain.command.other.NodeConfigsGetCommand;
import org.orbit.component.runtime.tier3.domain.service.DomainService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class NodeConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		DomainService service = resource.getService(DomainService.class);

		String requestName = request.getRequestName();

		if (Requests.GET_NODE_CONFIGS.equals(requestName)) {
			return new NodeConfigsGetCommand(service, request);

		} else if (Requests.GET_NODE_CONFIG.equals(requestName)) {
			return new NodeConfigGetCommand(service, request);

		} else if (Requests.ADD_NODE_CONFIG.equals(requestName)) {
			return new NodeConfigAddCommand(service, request);

		} else if (Requests.UPDATE_NODE_CONFIG.equals(requestName)) {
			return new NodeConfigUpdateCommand(service, request);

		} else if (Requests.REMOVE_NODE_CONFIG.equals(requestName)) {
			return new NodeConfigRemoveCommand(service, request);
		}

		return null;
	}

}
