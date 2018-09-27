package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.runtime.common.ws.AbstractOrbitCommand;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class NodeStartWSCommand extends AbstractOrbitCommand<NodeControlService> implements WSCommand {

	public static String ID = "org.orbit.component.runtime.nodecontrol.NodeStartWSCommand";

	public NodeStartWSCommand() {
		super(NodeControlService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.START_NODE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = (request.getParameter("id") instanceof String) ? (String) request.getParameter("id") : null;
		Object clean = request.getParameter("-clean");

		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'id' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean exists = this.service.nodeExists(id);
		if (!exists) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Node '" + id + "' does not exist.", null);
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		String accessToken = null;

		Map<String, Object> options = new HashMap<String, Object>();
		if (clean != null) {
			if (Boolean.TRUE.equals(clean) || "true".equalsIgnoreCase(clean.toString())) {
				options.put("clean", Boolean.TRUE);
			}
		}

		boolean succeed = this.service.startNode(id, accessToken, options);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
