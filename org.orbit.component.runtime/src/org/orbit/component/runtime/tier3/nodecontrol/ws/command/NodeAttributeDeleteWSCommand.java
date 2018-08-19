package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class NodeAttributeDeleteWSCommand extends AbstractWSCommand {

	protected NodeControlService service;

	public NodeAttributeDeleteWSCommand(NodeControlService service) {
		this.service = service;
	}

	@Override
	public boolean isSupported(Request request) {
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = (request.getParameter("id") instanceof String) ? (String) request.getParameter("id") : null;
		String name = (request.getParameter("name") instanceof String) ? (String) request.getParameter("name") : null;

		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'id' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'name' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean exists = this.service.nodeExists(id);
		if (!exists) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Node with id '" + id + "' is not found.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean succeed = this.service.deleteAttribute(id, name);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
