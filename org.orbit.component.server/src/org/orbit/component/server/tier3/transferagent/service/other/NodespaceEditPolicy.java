package org.orbit.component.server.tier3.transferagent.service.other;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.command.other.NodespaceCreateCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodespaceDeleteCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodespaceExistCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodespaceGetCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodespaceListCommand;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class NodespaceEditPolicy extends AbstractWSEditPolicyV1 {

	protected TransferAgentService service;

	public NodespaceEditPolicy(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		String requestName = request.getRequestName();
		if (OrbitConstants.Requests.GET_NODESPACES.equals(requestName)) {
			return new NodespaceListCommand(service, request);

		} else if (OrbitConstants.Requests.GET_NODESPACE.equals(requestName)) {
			return new NodespaceGetCommand(service, request);

		} else if (OrbitConstants.Requests.NODESPACE_EXIST.equals(requestName)) {
			return new NodespaceExistCommand(service, request);

		} else if (OrbitConstants.Requests.CREATE_NODESPACE.equals(requestName)) {
			return new NodespaceCreateCommand(service, request);

		} else if (OrbitConstants.Requests.DELETE_NODESPACE.equals(requestName)) {
			return new NodespaceDeleteCommand(service, request);
		}

		return null;
	}

}

// TransferAgentService service = super.getService(TransferAgentService.class);
// if (service == null) {
// return null;
// }
// TransferAgentService service = resource.getService(TransferAgentService.class);
