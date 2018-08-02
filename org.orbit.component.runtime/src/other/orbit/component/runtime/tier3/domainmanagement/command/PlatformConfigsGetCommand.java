package other.orbit.component.runtime.tier3.domainmanagement.command;

import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier3.domain.PlatformConfigDTO;
import org.orbit.component.runtime.model.domain.MachineConfig;
import org.orbit.component.runtime.model.domain.PlatformConfig;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
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

public class PlatformConfigsGetCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public PlatformConfigsGetCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		List<PlatformConfigDTO> taConfigDTOs = new ArrayList<PlatformConfigDTO>();
		try {
			String machineId = (String) this.request.getParameter("machineId");

			if (machineId == null) {
				// When machineId is not specified, get all transfer agents from all machines.
				List<MachineConfig> machineConfigs = this.service.getMachineConfigs();
				for (MachineConfig machineConfig : machineConfigs) {
					String currMachineId = machineConfig.getId();
					List<PlatformConfig> taConfigs = this.service.getPlatformConfigs(currMachineId);
					if (taConfigs != null && !taConfigs.isEmpty()) {
						for (PlatformConfig taConfig : taConfigs) {
							PlatformConfigDTO taConfigDTO = ModelConverter.Domain.toDTO(taConfig);
							taConfigDTO.setMachineId(currMachineId);
							taConfigDTOs.add(taConfigDTO);
						}
					}
				}

			} else {
				// When machineId is specified, get transfer agents from the machine.
				List<PlatformConfig> taConfigs = this.service.getPlatformConfigs(machineId);
				if (taConfigs != null) {
					for (PlatformConfig taConfig : taConfigs) {
						PlatformConfigDTO taConfigDTO = ModelConverter.Domain.toDTO(taConfig);
						taConfigDTO.setMachineId(machineId);
						taConfigDTOs.add(taConfigDTO);
					}
				}
			}

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = new Response(Response.SUCCESS, "Transfer Agent configs are retrieved.");
		response.setBody(taConfigDTOs);
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}
