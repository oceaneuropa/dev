package org.orbit.component.runtime.tier3.transferagent.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.transferagent.ModelConverter;
import org.orbit.component.model.tier3.transferagent.dto.INodeDTO;
import org.orbit.component.runtime.tier3.transferagent.resource.WorkspaceNodeHelper;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;
import org.origin.core.resources.node.INode;

public class NodeListWSCommand extends AbstractWSCommand {

	protected TransferAgentService service;

	public NodeListWSCommand(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		List<INodeDTO> nodeDTOs = new ArrayList<INodeDTO>();

		List<INode> nodes = WorkspaceNodeHelper.INSTANCE.getNodes(this.service.getNodeWorkspace());
		for (INode node : nodes) {
			INodeDTO nodeDTO = ModelConverter.getInstance().toDTO(node);
			nodeDTOs.add(nodeDTO);
		}

		return Response.status(Status.OK).entity(nodeDTOs).build();
	}

}
