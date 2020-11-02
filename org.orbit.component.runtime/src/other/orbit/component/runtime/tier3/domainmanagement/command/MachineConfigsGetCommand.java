package other.orbit.component.runtime.tier3.domainmanagement.command;

import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.runtime.model.domain.MachineConfig;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.util.RuntimeModelConverter;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.CommandResult;
import org.origin.common.command.impl.CommandResultImpl;
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
	public CommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		List<MachineConfigDTO> machineConfigDTOs = new ArrayList<MachineConfigDTO>();
		try {
			List<MachineConfig> machineConfigs = this.service.getMachineConfigs();
			if (machineConfigs != null) {
				for (MachineConfig machineConfig : machineConfigs) {
					MachineConfigDTO machineConfigDTO = RuntimeModelConverter.Domain.toMachineConfigDTO(machineConfig);
					machineConfigDTOs.add(machineConfigDTO);
				}
			}

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResultImpl(response);
		}

		Response response = new Response(Response.SUCCESS, "Machine configs is retrieved.");
		response.setBody(machineConfigDTOs);
		responses.setResponse("response", response);

		return new CommandResultImpl(response);
	}

}
