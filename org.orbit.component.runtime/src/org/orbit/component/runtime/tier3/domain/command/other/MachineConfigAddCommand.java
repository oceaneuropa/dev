package org.orbit.component.runtime.tier3.domain.command.other;

import org.orbit.component.model.tier3.domain.MachineConfigRTO;
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

public class MachineConfigAddCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public MachineConfigAddCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		boolean succeed = false;
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String name = (String) this.request.getParameter("name");
			String ipAddress = (String) this.request.getParameter("ipAddress");

			if (this.service.machineConfigExists(machineId)) {
				Response response = new Response(Response.FAILURE, "Machine already exist.");
				responses.setResponse(response);
				return new CommandResult(response);
			}

			MachineConfigRTO addMachineRequest = new MachineConfigRTO();
			addMachineRequest.setId(machineId);
			addMachineRequest.setName(name);
			addMachineRequest.setIpAddress(ipAddress);

			succeed = this.service.addMachineConfig(addMachineRequest);

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);

			// throw new CommandException(e);
			return new CommandResult(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Machine is added.");
		} else {
			response = new Response(Response.FAILURE, "Machine is not added.");
		}
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}
