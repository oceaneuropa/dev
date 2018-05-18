package other.orbit.component.runtime.tier3.nodecontrol.editpolicy;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

import other.orbit.component.runtime.tier3.nodecontrol.command.NodeCreateCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodeDeleteCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodeExistCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodeGetCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodeListCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodeStartCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodeStopCommandV1;

public class NodeEditPolicyV1 extends AbstractWSEditPolicyV1 {

	protected NodeControlService service;

	public NodeEditPolicyV1(NodeControlService service) {
		this.service = service;
	}

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		String requestName = request.getRequestName();
		if (Requests.GET_NODES.equals(requestName)) {
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
