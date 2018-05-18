package other.orbit.component.runtime.tier3.nodecontrol.command;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.resources.IPath;
import org.origin.common.resources.node.INodespace;
import org.origin.common.resources.node.NodespaceDescription;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class NodespaceCreateCommandV1 extends AbstractTransferAgentCommandV1 {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceCreateCommandV1(NodeControlService service, Request request) {
		super(service, request);
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");
		String nodespaceName = (String) this.request.getParameter("nodespace");
		System.out.println("    nodespaceName = " + nodespaceName);

		Responses responses = context.getAdapter(Responses.class);
		try {
			INodespace nodespace = this.service.getWorkspace().findRootMember(nodespaceName, INodespace.class);
			if (nodespace == null) {
				IPath nodespaceFullpath = IPath.ROOT.append(nodespaceName);
				nodespace = this.service.getWorkspace().getFolder(nodespaceFullpath, INodespace.class);
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
