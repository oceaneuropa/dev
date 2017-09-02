package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.core.resources.node.INodespace;

public class NodespaceExistCommand extends AbstractTransferAgentCommand {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceExistCommand(TransferAgentService service, Request request) {
		super(service, request);
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");
		String nodespaceName = (String) this.request.getParameter("nodespace");
		System.out.println("    nodespaceName = " + nodespaceName);

		Responses responses = context.getAdapter(Responses.class);
		try {
			boolean exist = false;
			INodespace nodespace = this.service.getNodespaceRoot().findRootMember(nodespaceName, INodespace.class);
			if (nodespace != null) {
				exist = true;
			}

			Response response = exist ? new Response(Response.SUCCESS, "Nodespace exists.") : new Response(Response.FAILURE, "Nodespace does not exist.");
			responses.setResponse(response);
			return new CommandResult(response);

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResult(response);
		}
	}

}
