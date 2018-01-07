package org.orbit.component.runtime.tier3.domain.command.other;

import org.orbit.component.model.tier3.domain.DomainException;
import org.orbit.component.model.tier3.domain.TransferAgentConfigRTO;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfigDTO;
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

public class TransferAgentConfigGetCommand extends AbstractCommand {

	protected DomainService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public TransferAgentConfigGetCommand(DomainService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		TransferAgentConfigDTO taConfigDTO = null;
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String id = (String) this.request.getParameter("id");

			TransferAgentConfigRTO taConfig = this.service.getTransferAgentConfig(machineId, id);
			if (taConfig != null) {
				taConfigDTO = ModelConverter.getInstance().toDTO(taConfig);
				taConfigDTO.setMachineId(machineId);
			}

		} catch (DomainException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = null;
		if (taConfigDTO != null) {
			response = new Response(Response.SUCCESS, "Transfer agent config is retrieved.");
			response.setBody(taConfigDTO);
		} else {
			response = new Response(Response.FAILURE, "Transfer agent config is not retrieved.");
		}
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}
