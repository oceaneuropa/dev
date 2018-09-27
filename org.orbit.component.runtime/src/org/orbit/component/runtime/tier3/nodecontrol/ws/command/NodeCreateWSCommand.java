package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.runtime.common.ws.AbstractOrbitCommand;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.resources.node.INode;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class NodeCreateWSCommand extends AbstractOrbitCommand<NodeControlService> implements WSCommand {

	public static String ID = "org.orbit.component.runtime.nodecontrol.NodeCreateWSCommand";

	public NodeCreateWSCommand() {
		super(NodeControlService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CREATE_NODE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = (request.getParameter("id") instanceof String) ? (String) request.getParameter("id") : null;
		String name = (request.getParameter("name") instanceof String) ? (String) request.getParameter("name") : null;
		String typeId = (request.getParameter("typeId") instanceof String) ? (String) request.getParameter("typeId") : null;

		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'id' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (typeId == null || typeId.isEmpty()) {
			typeId = "platform";
		}
		if (name == null || name.isEmpty()) {
			name = id;
		}

		boolean exists = this.service.nodeExists(id);
		if (exists) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Node with id '" + id + "' already exists.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		INode node = getService().createNode(id, typeId, name);
		boolean succeed = (node != null) ? true : false;

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
