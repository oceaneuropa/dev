package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.core.resources.IPath;
import org.origin.core.resources.node.INodespace;
import org.origin.core.resources.node.NodespaceDescription;

public class NodespaceCreateCommand extends AbstractTransferAgentCommand {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceCreateCommand(TransferAgentService service, Request request) {
		super(service, request);
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");
		String nodespaceName = (String) this.request.getParameter("nodespace");
		System.out.println("    nodespaceName = " + nodespaceName);

		Responses responses = context.getAdapter(Responses.class);
		try {
			INodespace nodespace = this.service.getNodespaceRoot().findRootMember(nodespaceName, INodespace.class);
			if (nodespace == null) {
				IPath nodespaceFullpath = IPath.ROOT.append(nodespaceName);
				nodespace = this.service.getNodespaceRoot().getFolder(nodespaceFullpath, INodespace.class);
			}

			if (nodespace.exists()) {
				// already exists
				Response response = new Response(Response.FAILURE, "Nodespace \"" + nodespaceName + "\" already exists.");
				responses.setResponse(response);
				return new CommandResult(response);
			}

			// Not exists
			// - create the nodespace folder (and its internal files)
			NodespaceDescription desc = new NodespaceDescription(nodespaceName);
			boolean succeed = nodespace.create(desc);

			Response response = succeed ? new Response(Response.SUCCESS, "Nodespace is created.") : new Response(Response.FAILURE, "Nodespace is not created.");
			responses.setResponse(response);
			return new CommandResult(response);

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResult(response);
		}
	}

}
