package org.orbit.component.runtime.tier4.missioncontrol.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class MissionGetWSCommand extends AbstractWSCommand implements WSCommand {

	protected MissionControlService service;

	public MissionGetWSCommand(MissionControlService service) {
		this.service = service;
	}

	@Override
	public boolean isSupported(Request request) {
		return false;
	}

	@Override
	public Response execute(Request request) {
		// String nodeId = (request.getParameter("nodeId") instanceof String) ? (String) request.getParameter("nodeId") : null;
		// if (nodeId == null || nodeId.isEmpty()) {
		// ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'nodeId' parameter is not set.");
		// return Response.status(Status.BAD_REQUEST).entity(error).build();
		// }
		//
		// INode node = WorkspaceNodeHelper.INSTANCE.getNode(this.service.getNodeWorkspace(), nodeId);
		// if (node == null) {
		// ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Node '" + nodeId + "' does not exist.");
		// return Response.status(Status.NOT_FOUND).entity(error).build();
		// }
		//
		// INodeDTO nodeDTO = ModelConverter.getInstance().toDTO(node);
		// return Response.status(Status.OK).entity(nodeDTO).build();
		return null;
	}

}
