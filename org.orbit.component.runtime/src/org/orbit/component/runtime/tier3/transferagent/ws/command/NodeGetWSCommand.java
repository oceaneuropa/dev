package org.orbit.component.runtime.tier3.transferagent.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.transferagent.dto.INodeDTO;
import org.orbit.component.runtime.tier3.transferagent.resource.WorkspaceNodeHelper;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.orbit.component.runtime.tier3.transferagent.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.core.resources.node.INode;

public class NodeGetWSCommand implements WSCommand {

	protected TransferAgentService service;

	public NodeGetWSCommand(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		String nodeId = (request.getParameter("nodeId") instanceof String) ? (String) request.getParameter("nodeId") : null;
		if (nodeId == null || nodeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'nodeId' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		INode node = WorkspaceNodeHelper.INSTANCE.getNode(this.service.getNodeWorkspace(), nodeId);
		if (node == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Node '" + nodeId + "' does not exist.");
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		INodeDTO nodeDTO = ModelConverter.getInstance().toDTO(node);
		return Response.status(Status.OK).entity(nodeDTO).build();
	}

}
