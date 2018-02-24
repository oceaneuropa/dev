package org.orbit.component.runtime.tier3.domain.command.other;

import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.common.rest.server.ServerException;

public class MachineConfigRemoveCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public MachineConfigRemoveCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		boolean succeed = false;
		try {
			String machineId = (String) this.request.getParameter("machineId");

			if (!this.service.machineConfigExists(machineId)) {
				ServerException exception = new ServerException("404", "Machine is not found.");
				Response response = new Response(Response.FAILURE, "Machine does not exist.", exception);
				responses.setResponse(response);
				return new CommandResult(response);
			}

			succeed = this.service.deleteMachineConfig(machineId);

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResult(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Machine is removed.");
		} else {
			response = new Response(Response.FAILURE, "Machine is not removed.");
		}
		responses.setResponse(response);

		return new CommandResult(response);
	}

}
