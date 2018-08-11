package other.orbit.component.runtime.tier3.domainmanagement.command;

import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.runtime.model.domain.NodeConfig;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.util.ModelConverter;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.common.rest.server.ServerException;

public class NodeConfigsGetCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodeConfigsGetCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		List<NodeConfigDTO> nodeConfigDTOs = new ArrayList<NodeConfigDTO>();
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String platformId = (String) this.request.getParameter("platformId");

			List<NodeConfig> nodeConfigs = this.service.getNodeConfigs(machineId, platformId);
			if (nodeConfigs != null) {
				for (NodeConfig nodeConfig : nodeConfigs) {
					NodeConfigDTO nodeConfigDTO = ModelConverter.Domain.toNodeConfigDTO(nodeConfig);
					nodeConfigDTO.setMachineId(machineId);
					nodeConfigDTO.setPlatformId(platformId);
					nodeConfigDTOs.add(nodeConfigDTO);
				}
			}

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = new Response(Response.SUCCESS, "Node configs are retrieved.");
		response.setBody(nodeConfigDTOs);
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}
