package org.orbit.component.runtime.tier3.nodecontrol.ws.editpolicy.other;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.other.NodespaceCreateCommandV1;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.other.NodespaceDeleteCommandV1;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.other.NodespaceExistCommandV1;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.other.NodespaceGetCommandV1;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.other.NodespaceListCommandV1;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class NodespaceEditPolicyV1 extends AbstractWSEditPolicyV1 {

	protected NodeControlService service;

	public NodespaceEditPolicyV1(NodeControlService service) {
		this.service = service;
	}

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		String requestName = request.getRequestName();
		if (Requests.GET_NODESPACES.equals(requestName)) {
			return new NodespaceListCommandV1(service, request);

		} else if (Requests.GET_NODESPACE.equals(requestName)) {
			return new NodespaceGetCommandV1(service, request);

		} else if (Requests.NODESPACE_EXIST.equals(requestName)) {
			return new NodespaceExistCommandV1(service, request);

		} else if (Requests.CREATE_NODESPACE.equals(requestName)) {
			return new NodespaceCreateCommandV1(service, request);

		} else if (Requests.DELETE_NODESPACE.equals(requestName)) {
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
