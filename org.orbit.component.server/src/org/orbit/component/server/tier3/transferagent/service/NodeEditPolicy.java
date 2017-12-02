package org.orbit.component.server.tier3.transferagent.service;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.command.NodeCreateCommand;
import org.orbit.component.server.tier3.transferagent.command.NodeDeleteCommand;
import org.orbit.component.server.tier3.transferagent.command.NodeListCommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class NodeEditPolicy extends AbstractWSEditPolicy {

	@Override
	public WSCommand getCommand(Request request) {
		TransferAgentService service = getService(TransferAgentService.class);
		if (service == null) {
			return null;
		}

		String requestName = request.getRequestName();

		if (OrbitConstants.Requests.GET_NODES.equals(requestName)) {
			return new NodeListCommand(service);

		} else if (OrbitConstants.Requests.CREATE_NODE.equals(requestName)) {
			return new NodeCreateCommand(service);

		} else if (OrbitConstants.Requests.DELETE_NODE.equals(requestName)) {
			return new NodeDeleteCommand(service);
		}

		return null;
	}

}
