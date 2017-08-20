package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.core.resources.IResource;
import org.origin.core.resources.node.INodespace;

public class NodespaceExistCommand extends AbstractCommand {

	protected TransferAgentService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceExistCommand(TransferAgentService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");

		Responses responses = context.getAdapter(Responses.class);

		boolean exist = false;
		try {
			String nodespaceName = (String) this.request.getParameter("nodespace");
			System.out.println("    nodespaceName = " + nodespaceName);

			IResource[] resources = this.service.getNodespaceRoot().getRootMembers();
			if (resources != null) {
				for (IResource resource : resources) {
					if (resource instanceof INodespace) {
						INodespace nodespace = (INodespace) resource;
						String currNodespaceName = nodespace.getName();
						if (nodespaceName != null && nodespaceName.equals(currNodespaceName)) {
							exist = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = null;
		if (exist) {
			response = new Response(Response.SUCCESS, "Nodespace exists.");
		} else {
			response = new Response(Response.FAILURE, "Nodespace does not exist.");
		}
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}
