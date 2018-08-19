package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.resources.node.INode;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.RequestUtil;

public class NodeAttributeUpdateWSCommand extends AbstractWSCommand {

	protected NodeControlService service;

	public NodeAttributeUpdateWSCommand(NodeControlService service) {
		this.service = service;
	}

	@Override
	public boolean isSupported(Request request) {
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = RequestUtil.getParameter(request, "id", String.class, "");
		String oldName = RequestUtil.getParameter(request, "oldName", String.class, "");
		String name = RequestUtil.getParameter(request, "name", String.class, "");
		Object value = RequestUtil.getParameter(request, "value", null);

		if (id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'id' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (name.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'name' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		INode node = this.service.getNode(id);
		if (node == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Node with id '" + id + "' is not found.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean isNameChanged = (oldName != null && !oldName.equals(name)) ? true : false;
		if (isNameChanged) {
			if (node.getDescription().hasAttribute(name)) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Attribute with name '" + name + "' already exists.", null);
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		boolean succeed = this.service.updateAttribute(id, oldName, name, value);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
