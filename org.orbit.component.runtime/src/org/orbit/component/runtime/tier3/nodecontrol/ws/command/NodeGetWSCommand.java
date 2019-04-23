package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.model.tier3.nodecontrol.NodeDTO;
import org.orbit.component.runtime.common.ws.AbstractOrbitCommand;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.util.RuntimeModelConverter;
import org.origin.common.resources.node.INode;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class NodeGetWSCommand extends AbstractOrbitCommand<NodeControlService> implements WSCommand {

	public static String ID = "org.orbit.component.runtime.nodecontrol.NodeGetWSCommand";

	public NodeGetWSCommand() {
		super(NodeControlService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.GET_NODE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = (request.getParameter("id") instanceof String) ? (String) request.getParameter("id") : null;
		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		INode node = getService().getNode(id);
		if (node == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Node '" + id + "' does not exist.");
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		NodeDTO nodeDTO = RuntimeModelConverter.NodeControl.toDTO(node);
		return Response.status(Status.OK).entity(nodeDTO).build();
	}

}
