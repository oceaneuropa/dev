package org.orbit.component.runtime.tier4.mission.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.component.runtime.tier4.mission.service.MissionControlService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class MissionCreateWSCommand extends AbstractWSCommand {

	protected MissionControlService service;

	public MissionCreateWSCommand(MissionControlService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
//		String nodeId = (request.getParameter("nodeId") instanceof String) ? (String) request.getParameter("nodeId") : null;
//		if (nodeId == null || nodeId.isEmpty()) {
//			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'nodeId' parameter is not set.", null);
//			return Response.status(Status.BAD_REQUEST).entity(error).build();
//		}
//
//		IWorkspace workspace = this.service.getNodeWorkspace();
//
//		boolean exists = WorkspaceNodeHelper.INSTANCE.nodeExists(workspace, nodeId);
//		if (exists) {
//			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Node '" + nodeId + "' already exists.", null);
//			return Response.status(Status.BAD_REQUEST).entity(error).build();
//		}
//
//		boolean succeed = false;
//		try {
//			succeed = WorkspaceNodeBuilder.INSTANCE.createNode(workspace, nodeId);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//
//			ErrorDTO error = handleError(e, "500", true);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
//		}
//
//		Map<String, Boolean> result = new HashMap<String, Boolean>();
//		result.put("succeed", succeed);
//		return Response.status(Status.OK).entity(result).build();
		
		
		
		
		return null;
	}

}
