package other.orbit.component.runtime.tier3.domainmanagement.editpolicy;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

import other.orbit.component.runtime.tier3.domainmanagement.command.NodeConfigAddCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.NodeConfigGetCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.NodeConfigRemoveCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.NodeConfigUpdateCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.NodeConfigsGetCommand;

public class NodeConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		DomainManagementService service = resource.getService(DomainManagementService.class);

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