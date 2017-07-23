package org.orbit.component.server.tier3.domain.command;

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.ModelConverter;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class MachineConfigGetCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public MachineConfigGetCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		MachineConfigDTO machineConfigDTO = null;
		try {
			String machineId = (String) this.request.getParameter("machineId");

			MachineConfigRTO machineConfig = this.service.getMachineConfig(machineId);
			if (machineConfig != null) {
				machineConfigDTO = ModelConverter.getInstance().toDTO(machineConfig);
			}

		} catch (DomainMgmtException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = null;
		if (machineConfigDTO != null) {
			response = new Response(Response.SUCCESS, "Machine config is retrieved.");
			response.setBody(machineConfigDTO);
		} else {
			response = new Response(Response.FAILURE, "Machine config is not retrieved.");
		}
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}