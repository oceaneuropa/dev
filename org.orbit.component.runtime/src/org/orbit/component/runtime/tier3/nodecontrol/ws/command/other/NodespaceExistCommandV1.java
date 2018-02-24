package org.orbit.component.runtime.tier3.nodecontrol.ws.command.other;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeManagementService;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.core.resources.node.INodespace;

public class NodespaceExistCommandV1 extends AbstractTransferAgentCommandV1 {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceExistCommandV1(NodeManagementService service, Request request) {
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
			INodespace nodespace = this.service.getNodeWorkspace().findRootMember(nodespaceName, INodespace.class);
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
