package org.orbit.component.server.tier3.transferagent.editpolicy;

import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier3.transferagent.command.NodespaceCreateCommand;
import org.orbit.component.server.tier3.transferagent.command.NodespaceDeleteCommand;
import org.orbit.component.server.tier3.transferagent.command.NodespaceExistCommand;
import org.orbit.component.server.tier3.transferagent.command.NodespaceGetCommand;
import org.orbit.component.server.tier3.transferagent.command.NodespaceListCommand;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.agent.AbstractWSEditPolicy;
import org.origin.common.rest.agent.CommonWSApplicationResource;
import org.origin.common.rest.model.Request;

public class NodespaceEditPolicy extends AbstractWSEditPolicy {

	@Override
	public ICommand getCommand(CommonWSApplicationResource resource, Request request) {
		// TransferAgentService service = super.getService(TransferAgentService.class);
		// if (service == null) {
		// return null;
		// }
		TransferAgentService service = resource.getService(TransferAgentService.class);

		String requestName = request.getRequestName();
		if (OrbitConstants.Requests.GET_NODESPACES.equals(requestName)) {
			return new NodespaceListCommand(service, request);

		} else if (OrbitConstants.Requests.GET_NODESPACE.equals(requestName)) {
			return new NodespaceGetCommand(service, request);

		} else if (OrbitConstants.Requests.NODESPACE_EXIST.equals(requestName)) {
			return new NodespaceExistCommand(service, request);

		} else if (OrbitConstants.Requests.CREATE_NODESPACE.equals(requestName)) {
			return new NodespaceCreateCommand(service, request);

		} else if (OrbitConstants.Requests.DELETE_NODESPACE.equals(requestName)) {
			return new NodespaceDeleteCommand(service, request);
		}

		return null;
	}

}
