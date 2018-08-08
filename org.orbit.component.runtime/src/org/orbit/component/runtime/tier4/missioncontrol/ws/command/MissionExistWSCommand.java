package org.orbit.component.runtime.tier4.missioncontrol.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class MissionExistWSCommand extends AbstractWSCommand implements WSCommand {

	protected MissionControlService service;

	public MissionExistWSCommand(MissionControlService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		// String nodeId = (request.getParameter("nodeId") instanceof String) ? (String) request.getParameter("nodeId") : null;
		// if (nodeId == null || nodeId.isEmpty()) {
		// ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'nodeId' parameter is not set.", null);
		// return Response.status(Status.BAD_REQUEST).entity(error).build();
		// }
		//
		// boolean exists = WorkspaceNodeHelper.INSTANCE.nodeExists(this.service.getNodeWorkspace(), nodeId);
		//
		// Map<String, Boolean> result = new HashMap<String, Boolean>();
		// result.put("exists", exists);
		// return Response.status(Status.OK).entity(result).build();
		return null;
	}

}
