package other.orbit.component.runtime.tier3.domainmanagement.command;

import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.orbit.component.runtime.tier3.domainmanagement.ws.DomainServiceModelConverter;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.common.rest.server.ServerException;

public class MachineConfigsGetCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public MachineConfigsGetCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		List<MachineConfigDTO> machineConfigDTOs = new ArrayList<MachineConfigDTO>();
		try {
			List<MachineConfigRTO> machineConfigs = this.service.getMachineConfigs();
			if (machineConfigs != null) {
				for (MachineConfigRTO machineConfig : machineConfigs) {
					MachineConfigDTO machineConfigDTO = DomainServiceModelConverter.getInstance().toDTO(machineConfig);
					machineConfigDTOs.add(machineConfigDTO);
				}
			}

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = new Response(Response.SUCCESS, "Machine configs is retrieved.");
		response.setBody(machineConfigDTOs);
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}
