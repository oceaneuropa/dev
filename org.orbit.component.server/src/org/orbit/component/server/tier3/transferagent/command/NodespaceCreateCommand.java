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
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.node.INodespace;

public class NodespaceCreateCommand extends AbstractCommand {

	protected TransferAgentService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceCreateCommand(TransferAgentService service, Request request) {
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

			INodespace theNodespace = null;
			IResource[] members = this.service.getNodespaceRoot().getRootMembers();
			for (IResource member : members) {
				if (member instanceof IFolder) {
					String folderName = ((IFolder) member).getName();

					if (nodespaceName.equals(folderName)) {
						if (member instanceof INodespace) {
							theNodespace = (INodespace) member;
						}
						break;
					}
				}
			}

			if (theNodespace == null) {
				IPath nodespaceFullpath = IPath.ROOT.append(nodespaceName);
				theNodespace = this.service.getNodespaceRoot().getFolder(nodespaceFullpath, INodespace.class);
			}

			if (theNodespace.exists()) {
				// already exists
				Response response = new Response(Response.FAILURE, "Nodespace \"" + nodespaceName + "\" already exists.");
				responses.setResponse(response);
				return new CommandResult(response);

			} else {
				// not exists
				// - create the nodespace folder (and internal folder/file)
				succeed = theNodespace.create();
			}

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResult(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Nodespace is created.");
		} else {
			response = new Response(Response.FAILURE, "Nodespace is not created.");
		}
		responses.setResponse(response);

		return new CommandResult(response);
	}

}
