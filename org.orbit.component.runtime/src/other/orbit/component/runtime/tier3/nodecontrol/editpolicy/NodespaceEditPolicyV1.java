package other.orbit.component.runtime.tier3.nodecontrol.editpolicy;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.command.Command;
import org.origin.common.rest.editpolicy.other.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.other.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

import other.orbit.component.runtime.tier3.nodecontrol.command.NodespaceCreateCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodespaceDeleteCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodespaceExistCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodespaceGetCommandV1;
import other.orbit.component.runtime.tier3.nodecontrol.command.NodespaceListCommandV1;

public class NodespaceEditPolicyV1 extends AbstractWSEditPolicyV1 {

	protected NodeControlService service;

	public NodespaceEditPolicyV1(NodeControlService service) {
		this.service = service;
	}

	@Override
	public Command getCommand(EditpolicyWSApplicationResource resource, Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.GET_NODESPACES.equals(requestName)) {
			return new NodespaceListCommandV1(service, request);

		} else if (RequestConstants.GET_NODESPACE.equals(requestName)) {
			return new NodespaceGetCommandV1(service, request);

		} else if (RequestConstants.NODESPACE_EXIST.equals(requestName)) {
			return new NodespaceExistCommandV1(service, request);

		} else if (RequestConstants.CREATE_NODESPACE.equals(requestName)) {
			return new NodespaceCreateCommandV1(service, request);

		} else if (RequestConstants.DELETE_NODESPACE.equals(requestName)) {
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
