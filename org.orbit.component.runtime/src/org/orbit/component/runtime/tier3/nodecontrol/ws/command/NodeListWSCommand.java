package org.orbit.component.runtime.tier3.nodecontrol.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.nodecontrol.INodeDTO;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.util.NodeControlConverter;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.util.WorkspaceHelper;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class NodeListWSCommand extends AbstractWSCommand {

	protected NodeControlService service;

	public NodeListWSCommand(NodeControlService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) throws Exception {
		List<INodeDTO> nodeDTOs = new ArrayList<INodeDTO>();

		List<INode> nodes = WorkspaceHelper.INSTANCE.getRootNodes(this.service.getWorkspace());
		for (INode node : nodes) {
			INodeDTO nodeDTO = NodeControlConverter.getInstance().toDTO(node);
			nodeDTOs.add(nodeDTO);
		}

		return Response.status(Status.OK).entity(nodeDTOs).build();
	}

}
