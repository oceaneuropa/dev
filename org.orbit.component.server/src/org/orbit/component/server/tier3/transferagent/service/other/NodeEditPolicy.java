package org.orbit.component.server.tier3.transferagent.service.other;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.command.other.NodeCreateCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodeDeleteCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodeExistCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodeGetCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodeListCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodeStartCommand;
import org.orbit.component.server.tier3.transferagent.command.other.NodeStopCommand;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

public class NodeEditPolicy extends AbstractWSEditPolicyV1 {

	protected TransferAgentService service;

	public NodeEditPolicy(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		String requestName = request.getRequestName();
		if (OrbitConstants.Requests.GET_NODES.equals(requestName)) {
			return new NodeListCommand(service, request);

		} else if (OrbitConstants.Requests.GET_NODE.equals(requestName)) {
			return new NodeGetCommand(service, request);

		} else if (OrbitConstants.Requests.NODE_EXIST.equals(requestName)) {
			return new NodeExistCommand(service, request);

		} else if (OrbitConstants.Requests.CREATE_NODE.equals(requestName)) {
			return new NodeCreateCommand(service, request);

		} else if (OrbitConstants.Requests.DELETE_NODE.equals(requestName)) {
			return new NodeDeleteCommand(service, request);

		} else if (OrbitConstants.Requests.START_NODE.equals(requestName)) {
			return new NodeStartCommand(service, request);

		} else if (OrbitConstants.Requests.STOP_NODE.equals(requestName)) {
			return new NodeStopCommand(service, request);
		}

		return null;
	}

	// TransferAgentService service = super.getService(TransferAgentService.class);
	// if (service == null) {
	// return null;
	// }

}
