package org.orbit.component.runtime.tier3.transferagent.ws.command.other;

import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier3.transferagent.dto.INodeDTO;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.orbit.component.runtime.tier3.transferagent.util.NodeModelConverter;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.core.resources.IResource;
import org.origin.core.resources.node.INode;
import org.origin.core.resources.node.INodespace;

public class NodeListCommandV1 extends AbstractTransferAgentCommandV1 {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodeListCommandV1(TransferAgentService service, Request request) {
		super(service, request);
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");
		String nodespaceName = (String) this.request.getParameter("nodespace");
		System.out.println("    nodespaceName = " + nodespaceName);

		Responses responses = context.getAdapter(Responses.class);
		try {
			INodespace nodespace = this.service.getNodeWorkspace().findRootMember(nodespaceName, INodespace.class);
			if (nodespace == null) {
				// nodespace doesn't exists
				Response response = new Response(Response.FAILURE, "Nodespace \"" + nodespaceName + "\" doesn't exist.");
				responses.setResponse(response);
				return new CommandResult(response);
			}

			List<INodeDTO> nodeDTOs = new ArrayList<INodeDTO>();
			IResource[] resources = nodespace.getMembers();
			for (IResource resource : resources) {
				if (resource instanceof INode) {
					INode node = (INode) resource;
					INodeDTO nodeDTO = NodeModelConverter.getInstance().toDTO(node);
					nodeDTOs.add(nodeDTO);
				}
			}

			Response response = new Response(Response.SUCCESS, "Nodes are retrieved.");
			response.setBody(nodeDTOs);
			responses.setResponse(response);
			return new CommandResult(response);

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResult(response);
		}
	}

}
