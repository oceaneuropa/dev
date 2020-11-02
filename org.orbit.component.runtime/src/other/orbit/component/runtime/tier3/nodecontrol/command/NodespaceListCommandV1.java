package other.orbit.component.runtime.tier3.nodecontrol.command;

import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier3.nodecontrol.NodespaceDTO;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.util.RuntimeModelConverter;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.CommandResult;
import org.origin.common.command.impl.CommandResultImpl;
import org.origin.common.resources.IResource;
import org.origin.common.resources.node.INodespace;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class NodespaceListCommandV1 extends AbstractTransferAgentCommandV1 {

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodespaceListCommandV1(NodeControlService service, Request request) {
		super(service, request);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		System.out.println(getClass().getSimpleName() + ".execute()");

		Responses responses = context.getAdapter(Responses.class);
		try {
			List<NodespaceDTO> nodespaceDTOs = new ArrayList<NodespaceDTO>();
			IResource[] resources = this.service.getWorkspace().getRootMembers();
			for (IResource resource : resources) {
				if (resource instanceof INodespace) {
					INodespace nodespace = (INodespace) resource;
					NodespaceDTO nodespaceDTO = RuntimeModelConverter.NodeControl.toDTO(nodespace);
					nodespaceDTOs.add(nodespaceDTO);
				}
			}

			Response response = new Response(Response.SUCCESS, "Nodespaces are retrieved.");
			response.setBody(nodespaceDTOs);
			responses.setResponse(response);
			return new CommandResultImpl(response);

		} catch (Exception e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResultImpl(response);
		}
	}

}
