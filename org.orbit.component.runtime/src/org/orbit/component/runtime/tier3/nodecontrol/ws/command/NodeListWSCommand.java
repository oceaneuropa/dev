package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.model.tier3.nodecontrol.NodeDTO;
import org.orbit.component.runtime.common.ws.AbstractOrbitCommand;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.util.ModelConverter;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.util.WorkspaceHelper;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class NodeListWSCommand extends AbstractOrbitCommand<NodeControlService> implements WSCommand {

	public static String ID = "org.orbit.component.runtime.nodecontrol.NodeListWSCommand";

	public NodeListWSCommand() {
		super(NodeControlService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.GET_NODES.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		List<NodeDTO> nodeDTOs = new ArrayList<NodeDTO>();

		List<INode> nodes = WorkspaceHelper.INSTANCE.getRootNodes(getService().getWorkspace());
		for (INode node : nodes) {
			NodeDTO nodeDTO = ModelConverter.NodeControl.toDTO(node);
			nodeDTOs.add(nodeDTO);
		}

		return Response.status(Status.OK).entity(nodeDTOs).build();
	}

}
