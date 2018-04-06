package org.orbit.component.runtime.tier4.missioncontrol.ws.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.util.WorkspaceHelper;
import org.origin.common.resources.util.WorkspaceUtil;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class MissionDeleteWSCommand extends AbstractWSCommand {

	protected MissionControlService service;

	public MissionDeleteWSCommand(MissionControlService service) {
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
//		if (!exists) {
//			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Node '" + nodeId + "' does not exist.", null);
//			return Response.status(Status.NOT_FOUND).entity(error).build();
//		}
//
//		boolean succeed = false;
//		try {
//			succeed = WorkspaceNodeBuilder.INSTANCE.deleteNode(workspace, nodeId);
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
