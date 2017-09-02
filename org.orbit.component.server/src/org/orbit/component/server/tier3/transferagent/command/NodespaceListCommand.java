package org.orbit.component.server.tier3.transferagent.command;

import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier3.transferagent.ModelConverter;
import org.orbit.component.model.tier3.transferagent.dto.INodespaceDTO;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.core.resources.IResource;
import org.origin.core.resources.node.INodespace;

public class NodespaceListCommand extends AbstractTransferAgentCommand {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceListCommand(TransferAgentService service, Request request) {
		super(service, request);
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");

		Responses responses = context.getAdapter(Responses.class);
		try {
			List<INodespaceDTO> nodespaceDTOs = new ArrayList<INodespaceDTO>();
			IResource[] resources = this.service.getNodespaceRoot().getRootMembers();
			for (IResource resource : resources) {
				if (resource instanceof INodespace) {
					INodespace nodespace = (INodespace) resource;
					INodespaceDTO nodespaceDTO = ModelConverter.getInstance().toDTO(nodespace);
					nodespaceDTOs.add(nodespaceDTO);
				}
			}

			Response response = new Response(Response.SUCCESS, "Nodespaces are retrieved.");
			response.setBody(nodespaceDTOs);
			responses.setResponse(response);
			return new CommandResult(response);

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResult(response);
		}
	}

}
