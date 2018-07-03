package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class NodeControlWSEditPolicy extends AbstractWSEditPolicy {

	public static final String ID = "transferagent.node.editpolicy";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public WSCommand getCommand(Request request) {
		NodeControlService service = getService(NodeControlService.class);
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

		} else if (Requests.UPDATE_NODE.equals(requestName)) {
			return new NodeUpdateWSCommand(service);

		} else if (Requests.DELETE_NODE.equals(requestName)) {
			return new NodeDeleteWSCommand(service);

		} else if (Requests.NODE_STATUS.equals(requestName)) {
			return new NodeStatusWSCommand(service);
		}

		return null;
	}

}
