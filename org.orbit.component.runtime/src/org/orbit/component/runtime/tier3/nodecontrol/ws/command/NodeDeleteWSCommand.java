package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier3.nodecontrol.resource.WorkspaceNodeBuilder;
import org.orbit.component.runtime.tier3.nodecontrol.resource.WorkspaceNodeHelper;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeManagementService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.core.resources.IWorkspace;

public class NodeDeleteWSCommand extends AbstractWSCommand {

	protected NodeManagementService service;

	public NodeDeleteWSCommand(NodeManagementService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String nodeId = (request.getParameter("nodeId") instanceof String) ? (String) request.getParameter("nodeId") : null;
		if (nodeId == null || nodeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'nodeId' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		IWorkspace workspace = this.service.getNodeWorkspace();
		boolean exists = WorkspaceNodeHelper.INSTANCE.nodeExists(workspace, nodeId);
		if (!exists) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Node '" + nodeId + "' does not exist.", null);
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		boolean succeed = WorkspaceNodeBuilder.INSTANCE.deleteNode(workspace, nodeId);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
