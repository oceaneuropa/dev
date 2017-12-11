package org.orbit.component.runtime.tier3.transferagent.ws.editpolicy;

import org.orbit.component.runtime.OrbitConstants;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.orbit.component.runtime.tier3.transferagent.ws.command.NodeCreateWSCommand;
import org.orbit.component.runtime.tier3.transferagent.ws.command.NodeDeleteWSCommand;
import org.orbit.component.runtime.tier3.transferagent.ws.command.NodeExistWSCommand;
import org.orbit.component.runtime.tier3.transferagent.ws.command.NodeGetWSCommand;
import org.orbit.component.runtime.tier3.transferagent.ws.command.NodeListWSCommand;
import org.orbit.component.runtime.tier3.transferagent.ws.command.NodeStatusWSCommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class NodeWSEditPolicy extends AbstractWSEditPolicy {

	public static final String ID = "transferagent.node.editpolicy";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public WSCommand getCommand(Request request) {
		TransferAgentService service = getService(TransferAgentService.class);
		if (service == null) {
			return null;
		}

		String requestName = request.getRequestName();

		if (OrbitConstants.Requests.LIST_NODES.equals(requestName)) {
			return new NodeListWSCommand(service);

		} else if (OrbitConstants.Requests.GET_NODE.equals(requestName)) {
			return new NodeGetWSCommand(service);

		} else if (OrbitConstants.Requests.NODE_EXIST.equals(requestName)) {
			return new NodeExistWSCommand(service);

		} else if (OrbitConstants.Requests.CREATE_NODE.equals(requestName)) {
			return new NodeCreateWSCommand(service);

		} else if (OrbitConstants.Requests.DELETE_NODE.equals(requestName)) {
			return new NodeDeleteWSCommand(service);

		} else if (OrbitConstants.Requests.NODE_STATUS.equals(requestName)) {
			return new NodeStatusWSCommand(service);
		}

		return null;
	}

}
