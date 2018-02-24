package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.nodecontrol.dto.INodeDTO;
import org.orbit.component.runtime.tier3.nodecontrol.resource.WorkspaceNodeHelper;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeManagementService;
import org.orbit.component.runtime.tier3.nodecontrol.util.NodeModelConverter;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;
import org.origin.core.resources.node.INode;

public class NodeListWSCommand extends AbstractWSCommand {

	protected NodeManagementService service;

	public NodeListWSCommand(NodeManagementService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) throws Exception {
		List<INodeDTO> nodeDTOs = new ArrayList<INodeDTO>();

		List<INode> nodes = WorkspaceNodeHelper.INSTANCE.getNodes(this.service.getNodeWorkspace());
		for (INode node : nodes) {
			INodeDTO nodeDTO = NodeModelConverter.getInstance().toDTO(node);
			nodeDTOs.add(nodeDTO);
		}

		return Response.status(Status.OK).entity(nodeDTOs).build();
	}

}
