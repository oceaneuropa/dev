package other.orbit.component.runtime.tier3.domainmanagement.command;

import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.runtime.model.domain.NodeConfig;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.util.RuntimeModelConverter;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.common.rest.server.ServerException;

public class NodeConfigGetCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodeConfigGetCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		NodeConfigDTO nodeConfigDTO = null;
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String platformId = (String) this.request.getParameter("platformId");
			String id = (String) this.request.getParameter("id");

			NodeConfig nodeConfig = this.service.getNodeConfig(machineId, platformId, id);
			if (nodeConfig != null) {
				nodeConfigDTO = RuntimeModelConverter.Domain.toNodeConfigDTO(nodeConfig);
			}

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = null;
		if (nodeConfigDTO != null) {
			response = new Response(Response.SUCCESS, "Node config is retrieved.");
			response.setBody(nodeConfigDTO);
		} else {
			response = new Response(Response.FAILURE, "Node config is not retrieved.");
		}
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}
