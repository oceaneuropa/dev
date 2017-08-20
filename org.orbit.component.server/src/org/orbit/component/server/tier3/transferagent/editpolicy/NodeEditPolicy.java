package org.orbit.component.server.tier3.transferagent.editpolicy;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.command.NodeCreateCommand;
import org.orbit.component.server.tier3.transferagent.command.NodeDeleteCommand;
import org.orbit.component.server.tier3.transferagent.command.NodeExistCommand;
import org.orbit.component.server.tier3.transferagent.command.NodeGetCommand;
import org.orbit.component.server.tier3.transferagent.command.NodeStartCommand;
import org.orbit.component.server.tier3.transferagent.command.NodeStopCommand;
import org.orbit.component.server.tier3.transferagent.command.NodesGetCommand;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.agent.AbstractWSEditPolicy;
import org.origin.common.rest.model.Request;

public class NodeEditPolicy extends AbstractWSEditPolicy {

	@Override
	public ICommand getCommand(Request request) {
		TransferAgentService service = super.getService(TransferAgentService.class);
		if (service == null) {
			return null;
		}

		String requestName = request.getRequestName();
		if (OrbitConstants.Requests.GET_NODES.equals(requestName)) {
			return new NodesGetCommand(service, request);

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

}
