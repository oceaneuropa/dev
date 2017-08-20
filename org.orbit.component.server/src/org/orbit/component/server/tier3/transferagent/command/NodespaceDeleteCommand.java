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
import org.origin.core.resources.IPath;
import org.origin.core.resources.node.INodespace;

public class NodespaceDeleteCommand extends AbstractCommand {

	protected TransferAgentService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceDeleteCommand(TransferAgentService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");
		Responses responses = context.getAdapter(Responses.class);

		boolean succeed = false;
		try {
			String nodespaceName = (String) this.request.getParameter("nodespace");
			System.out.println("    nodespaceName = " + nodespaceName);

			IPath nodespaceFullpath = IPath.ROOT.append(nodespaceName);

			INodespace nodespace = this.service.getNodespaceRoot().getFolder(nodespaceFullpath, INodespace.class);
			succeed = nodespace.delete();

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Nodespace is deleted.");
		} else {
			response = new Response(Response.FAILURE, "Nodespace is not deleted.");
		}
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}
