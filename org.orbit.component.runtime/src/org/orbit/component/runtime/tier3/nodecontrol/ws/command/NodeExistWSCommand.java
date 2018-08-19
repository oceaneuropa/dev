package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class NodeExistWSCommand extends AbstractWSCommand implements WSCommand {

	protected NodeControlService service;

	public NodeExistWSCommand(NodeControlService service) {
		this.service = service;
	}

	@Override
	public boolean isSupported(Request request) {
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = (request.getParameter("id") instanceof String) ? (String) request.getParameter("id") : null;
		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'id' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean exists = this.service.nodeExists(id);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
