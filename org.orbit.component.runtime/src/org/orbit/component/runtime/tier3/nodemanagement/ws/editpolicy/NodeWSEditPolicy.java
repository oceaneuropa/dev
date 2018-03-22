package org.orbit.component.runtime.tier3.nodemanagement.ws.editpolicy;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementService;
import org.orbit.component.runtime.tier3.nodemanagement.ws.command.NodeCreateWSCommand;
import org.orbit.component.runtime.tier3.nodemanagement.ws.command.NodeDeleteWSCommand;
import org.orbit.component.runtime.tier3.nodemanagement.ws.command.NodeExistWSCommand;
import org.orbit.component.runtime.tier3.nodemanagement.ws.command.NodeGetWSCommand;
import org.orbit.component.runtime.tier3.nodemanagement.ws.command.NodeListWSCommand;
import org.orbit.component.runtime.tier3.nodemanagement.ws.command.NodeStatusWSCommand;
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
		NodeManagementService service = getService(NodeManagementService.class);
		if (service == null) {
			return null;
		}

		String requestName = request.getRequestName();

		if (Requests.GET_NODES.equals(requestName)) {
			return new NodeListWSCommand(service);

		} else if (Requests.GET_NODE.equals(requestName)) {
			return new NodeGetWSCommand(service);

		} else if (Requests.NODE_EXIST.equals(requestName)) {
			return new NodeExistWSCommand(service);

		} else if (Requests.CREATE_NODE.equals(requestName)) {
			return new NodeCreateWSCommand(service);

		} else if (Requests.DELETE_NODE.equals(requestName)) {
			return new NodeDeleteWSCommand(service);

		} else if (Requests.NODE_STATUS.equals(requestName)) {
			return new NodeStatusWSCommand(service);
		}

		return null;
	}

}
