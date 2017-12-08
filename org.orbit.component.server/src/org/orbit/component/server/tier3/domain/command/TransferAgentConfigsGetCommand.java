package org.orbit.component.server.tier3.domain.command;

import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.ModelConverter;
import org.orbit.component.model.tier3.domain.TransferAgentConfigRTO;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfigDTO;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class TransferAgentConfigsGetCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public TransferAgentConfigsGetCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		List<TransferAgentConfigDTO> taConfigDTOs = new ArrayList<TransferAgentConfigDTO>();
		try {
			String machineId = (String) this.request.getParameter("machineId");

			if (machineId == null) {
				// When machineId is not specified, get all transfer agents from all machines.
				List<MachineConfigRTO> machineConfigs = this.service.getMachineConfigs();
				for (MachineConfigRTO machineConfig : machineConfigs) {
					String currMachineId = machineConfig.getId();
					List<TransferAgentConfigRTO> taConfigs = this.service.getTransferAgentConfigs(currMachineId);
					if (taConfigs != null && !taConfigs.isEmpty()) {
						for (TransferAgentConfigRTO taConfig : taConfigs) {
							TransferAgentConfigDTO taConfigDTO = ModelConverter.getInstance().toDTO(taConfig);
							taConfigDTO.setMachineId(currMachineId);
							taConfigDTOs.add(taConfigDTO);
						}
					}
				}

			} else {
				// When machineId is specified, get transfer agents from the machine.
				List<TransferAgentConfigRTO> taConfigs = this.service.getTransferAgentConfigs(machineId);
				if (taConfigs != null) {
					for (TransferAgentConfigRTO taConfig : taConfigs) {
						TransferAgentConfigDTO taConfigDTO = ModelConverter.getInstance().toDTO(taConfig);
						taConfigDTO.setMachineId(machineId);
						taConfigDTOs.add(taConfigDTO);
					}
				}
			}

		} catch (DomainMgmtException e) {
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
