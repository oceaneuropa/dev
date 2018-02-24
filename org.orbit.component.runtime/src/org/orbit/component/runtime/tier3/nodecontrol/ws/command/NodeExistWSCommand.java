package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier3.nodecontrol.resource.WorkspaceNodeHelper;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeManagementService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class NodeExistWSCommand implements WSCommand {

	protected NodeManagementService service;

	public NodeExistWSCommand(NodeManagementService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String nodeId = (request.getParameter("nodeId") instanceof String) ? (String) request.getParameter("nodeId") : null;
		if (nodeId == null || nodeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'nodeId' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean exists = WorkspaceNodeHelper.INSTANCE.nodeExists(this.service.getNodeWorkspace(), nodeId);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
