package other.orbit.component.runtime.tier3.domainmanagement.command;

import org.orbit.component.runtime.model.domain.MachineConfig;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.CommandResult;
import org.origin.common.command.impl.CommandResultImpl;
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
	public CommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		boolean succeed = false;
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String name = (String) this.request.getParameter("name");
			String ipAddress = (String) this.request.getParameter("ipAddress");

			if (this.service.machineConfigExists(machineId)) {
				Response response = new Response(Response.FAILURE, "Machine already exist.");
				responses.setResponse(response);
				return new CommandResultImpl(response);
			}

			MachineConfig addMachineRequest = new MachineConfig();
			addMachineRequest.setId(machineId);
			addMachineRequest.setName(name);
			addMachineRequest.setIpAddress(ipAddress);

			succeed = this.service.addMachineConfig(addMachineRequest);

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);

			// throw new CommandException(e);
			return new CommandResultImpl(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Machine is added.");
		} else {
			response = new Response(Response.FAILURE, "Machine is not added.");
		}
		responses.setResponse("response", response);

		return new CommandResultImpl(response);
	}

}
