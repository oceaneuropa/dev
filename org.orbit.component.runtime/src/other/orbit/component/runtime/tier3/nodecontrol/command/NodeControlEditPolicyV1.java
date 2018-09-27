package other.orbit.component.runtime.tier3.nodecontrol.command;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.rest.editpolicy.AbstractServiceEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class NodeControlEditPolicyV1 extends AbstractServiceEditPolicy {

	public static final String ID = "component.node_control.editpolicy";

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

		if (RequestConstants.GET_NODES.equals(requestName)) {
			// return new NodeListWSCommand(service);

		} else if (RequestConstants.GET_NODE.equals(requestName)) {
			// return new NodeGetWSCommand(service);

		} else if (RequestConstants.NODE_EXIST.equals(requestName)) {
			// return new NodeExistWSCommand(service);

		} else if (RequestConstants.CREATE_NODE.equals(requestName)) {
			// return new NodeCreateWSCommand(service);

		} else if (RequestConstants.UPDATE_NODE.equals(requestName)) {
			// return new NodeUpdateWSCommand(service);

		} else if (RequestConstants.DELETE_NODE.equals(requestName)) {
			// return new NodeDeleteWSCommand(service);

		} else if (RequestConstants.START_NODE.equals(requestName)) {
			// return new NodeStartWSCommand(service);

		} else if (RequestConstants.STOP_NODE.equals(requestName)) {
			// return new NodeStopWSCommand(service);

		} else if (RequestConstants.NODE_STATUS.equals(requestName)) {
			// return new NodeStatusWSCommand(service);

		} else if (RequestConstants.ADD_NODE_ATTR.equals(requestName)) {
			// return new NodeAttributeAddWSCommand(service);

		} else if (RequestConstants.UPDATE_NODE_ATTR.equals(requestName)) {
			// return new NodeAttributeUpdateWSCommand(service);

		} else if (RequestConstants.DELETE_NODE_ATTR.equals(requestName)) {
			// return new NodeAttributeDeleteWSCommand(service);
		}

		return null;
	}

}
