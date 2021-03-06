package other.orbit.component.runtime.tier3.nodecontrol.command;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.CommandResult;
import org.origin.common.command.impl.CommandResultImpl;
import org.origin.common.resources.IPath;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.INodespace;
import org.origin.common.resources.node.NodeDescription;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class NodeCreateCommandV1 extends AbstractTransferAgentCommandV1 {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodeCreateCommandV1(NodeControlService service, Request request) {
		super(service, request);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");
		String nodespaceName = (String) this.request.getParameter("nodespace");
		String nodeName = (String) this.request.getParameter("node");
		System.out.println("    nodespaceName = " + nodespaceName);
		System.out.println("    nodeName = " + nodeName);

		Responses responses = context.getAdapter(Responses.class);
		try {
			INodespace nodespace = this.service.getWorkspace().findRootMember(nodespaceName, INodespace.class);
			if (nodespace == null) {
				// nodespace doesn't exists
				Response response = new Response(Response.FAILURE, "Nodespace \"" + nodespaceName + "\" doesn't exist.");
				responses.setResponse(response);
				return new CommandResultImpl(response);
			}

			IPath nodeFullpath = nodespace.getFullPath().append(nodeName);
			INode node = this.service.getWorkspace().getFolder(nodeFullpath, INode.class);

			if (node.exists()) {
				// already exists
				Response response = new Response(Response.FAILURE, "Node \"" + nodeName + "\" already exists.");
				responses.setResponse(response);
				return new CommandResultImpl(response);
			}

			// Not exists
			// - create the nodespace folder (and its internal files)
			NodeDescription desc = new NodeDescription(nodeName);
			boolean succeed = node.create(desc);

			Response response = succeed ? new Response(Response.SUCCESS, "Node is created.") : new Response(Response.FAILURE, "Node is not created.");
			responses.setResponse(response);
			return new CommandResultImpl(response);

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResultImpl(response);
		}
	}

}
