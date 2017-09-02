package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.core.resources.node.INode;
import org.origin.core.resources.node.INodespace;

public class NodeDeleteCommand extends AbstractTransferAgentCommand {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodeDeleteCommand(TransferAgentService service, Request request) {
		super(service, request);
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");
		String nodespaceName = (String) this.request.getParameter("nodespace");
		String nodeName = (String) this.request.getParameter("node");
		System.out.println("    nodespaceName = " + nodespaceName);
		System.out.println("    nodeName = " + nodeName);

		Responses responses = context.getAdapter(Responses.class);
		try {
			INodespace nodespace = this.service.getNodespaceRoot().findRootMember(nodespaceName, INodespace.class);
			if (nodespace == null) {
				// nodespace doesn't exists
				Response response = new Response(Response.FAILURE, "Nodespace \"" + nodespaceName + "\" doesn't exist.");
				responses.setResponse(response);
				return new CommandResult(response);
			}

			INode node = nodespace.findMember(nodeName, INode.class);
			if (node == null) {
				// node doesn't exists
				Response response = new Response(Response.FAILURE, "Node \"" + nodeName + "\" doesn't exist.");
				responses.setResponse(response);
				return new CommandResult(response);
			}

			boolean succeed = node.delete();

			Response response = succeed ? new Response(Response.SUCCESS, "Node is deleted.") : new Response(Response.FAILURE, "Node is not deleted.");
			responses.setResponse(response);
			return new CommandResult(response);

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResult(response);
		}
	}

}
