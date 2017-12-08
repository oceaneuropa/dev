package org.orbit.component.server.tier3.transferagent.ws.editpolicy.other;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.orbit.component.server.tier3.transferagent.ws.command.other.NodespaceCreateCommandV1;
import org.orbit.component.server.tier3.transferagent.ws.command.other.NodespaceDeleteCommandV1;
import org.orbit.component.server.tier3.transferagent.ws.command.other.NodespaceExistCommandV1;
import org.orbit.component.server.tier3.transferagent.ws.command.other.NodespaceGetCommandV1;
import org.orbit.component.server.tier3.transferagent.ws.command.other.NodespaceListCommandV1;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class NodespaceEditPolicyV1 extends AbstractWSEditPolicyV1 {

	protected TransferAgentService service;

	public NodespaceEditPolicyV1(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		String requestName = request.getRequestName();
		if (OrbitConstants.Requests.GET_NODESPACES.equals(requestName)) {
			return new NodespaceListCommandV1(service, request);

		} else if (OrbitConstants.Requests.GET_NODESPACE.equals(requestName)) {
			return new NodespaceGetCommandV1(service, request);

		} else if (OrbitConstants.Requests.NODESPACE_EXIST.equals(requestName)) {
			return new NodespaceExistCommandV1(service, request);

		} else if (OrbitConstants.Requests.CREATE_NODESPACE.equals(requestName)) {
			return new NodespaceCreateCommandV1(service, request);

		} else if (OrbitConstants.Requests.DELETE_NODESPACE.equals(requestName)) {
			return new NodespaceDeleteCommandV1(service, request);
		}

		return null;
	}

}

// TransferAgentService service = super.getService(TransferAgentService.class);
// if (service == null) {
// return null;
// }
// TransferAgentService service = resource.getService(TransferAgentService.class);
