package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.nodecontrol.NodeDTO;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.util.NodeControlConverter;
import org.origin.common.resources.node.INode;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class NodeGetWSCommand implements WSCommand {

	protected NodeControlService service;

	public NodeGetWSCommand(NodeControlService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = (request.getParameter("id") instanceof String) ? (String) request.getParameter("id") : null;
		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		INode node = this.service.getNode(id);
		if (node == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Node '" + id + "' does not exist.");
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		NodeDTO nodeDTO = NodeControlConverter.getInstance().toDTO(node);
		return Response.status(Status.OK).entity(nodeDTO).build();
	}

}
