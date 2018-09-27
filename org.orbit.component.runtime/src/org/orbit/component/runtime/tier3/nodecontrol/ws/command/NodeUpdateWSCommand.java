package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.runtime.common.ws.AbstractOrbitCommand;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.NodeDescription;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.RequestUtil;

public class NodeUpdateWSCommand extends AbstractOrbitCommand<NodeControlService> implements WSCommand {

	public static String ID = "org.orbit.component.runtime.nodecontrol.NodeUpdateWSCommand";

	public NodeUpdateWSCommand() {
		super(NodeControlService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.UPDATE_NODE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = RequestUtil.getParameter(request, "id", String.class, "");
		String name = RequestUtil.getParameter(request, "name", String.class, "");
		String typeId = RequestUtil.getParameter(request, "typeId", String.class, "");

		boolean updateName = RequestUtil.getParameter(request, "update_name", Boolean.class, false);
		boolean updateType = RequestUtil.getParameter(request, "update_type", Boolean.class, false);

		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'id' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		INode node = getService().getNode(id);
		if (node == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Node with id '" + id + "' cannot be found.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean isNameUpdated = false;
		boolean isTypeUpdated = false;
		if (updateName) {
			isNameUpdated = node.rename(name);
		}
		if (updateType) {
			NodeDescription desc = node.getDescription();
			desc.setAttirbute("typeId", typeId);
			node.setDescription(desc);
			isTypeUpdated = true;
		}

		boolean succeed = (!updateName || isNameUpdated) && (!updateType || isTypeUpdated);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
