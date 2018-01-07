package org.orbit.component.runtime.tier3.domain.command.other;

import org.orbit.component.model.tier3.domain.DomainException;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.dto.MachineConfigDTO;
import org.orbit.component.runtime.tier3.domain.service.DomainService;
import org.orbit.component.runtime.tier3.domain.ws.ModelConverter;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class MachineConfigGetCommand extends AbstractCommand {

	protected DomainService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public MachineConfigGetCommand(DomainService service, Request request) {
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

		} catch (DomainException e) {
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
