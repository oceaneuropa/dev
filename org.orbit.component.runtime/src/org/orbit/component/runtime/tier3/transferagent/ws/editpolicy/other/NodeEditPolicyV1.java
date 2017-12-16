package org.orbit.component.runtime.tier3.transferagent.ws.editpolicy.other;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.orbit.component.runtime.tier3.transferagent.ws.command.other.NodeCreateCommandV1;
import org.orbit.component.runtime.tier3.transferagent.ws.command.other.NodeDeleteCommandV1;
import org.orbit.component.runtime.tier3.transferagent.ws.command.other.NodeExistCommandV1;
import org.orbit.component.runtime.tier3.transferagent.ws.command.other.NodeGetCommandV1;
import org.orbit.component.runtime.tier3.transferagent.ws.command.other.NodeListCommandV1;
import org.orbit.component.runtime.tier3.transferagent.ws.command.other.NodeStartCommandV1;
import org.orbit.component.runtime.tier3.transferagent.ws.command.other.NodeStopCommandV1;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class NodeEditPolicyV1 extends AbstractWSEditPolicyV1 {

	protected TransferAgentService service;

	public NodeEditPolicyV1(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		String requestName = request.getRequestName();
		if (Requests.LIST_NODES.equals(requestName)) {
			return new NodeListCommandV1(service, request);

		} else if (Requests.GET_NODE.equals(requestName)) {
			return new NodeGetCommandV1(service, request);

		} else if (Requests.NODE_EXIST.equals(requestName)) {
			return new NodeExistCommandV1(service, request);

		} else if (Requests.CREATE_NODE.equals(requestName)) {
			return new NodeCreateCommandV1(service, request);

		} else if (Requests.DELETE_NODE.equals(requestName)) {
			return new NodeDeleteCommandV1(service, request);

		} else if (Requests.START_NODE.equals(requestName)) {
			return new NodeStartCommandV1(service, request);

		} else if (Requests.STOP_NODE.equals(requestName)) {
			return new NodeStopCommandV1(service, request);
		}

		return null;
	}

	// TransferAgentService service = super.getService(TransferAgentService.class);
	// if (service == null) {
	// return null;
	// }

}
