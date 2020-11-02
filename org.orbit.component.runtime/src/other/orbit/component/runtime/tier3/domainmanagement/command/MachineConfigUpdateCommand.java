package other.orbit.component.runtime.tier3.domainmanagement.command;

import java.util.List;

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

public class MachineConfigUpdateCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public MachineConfigUpdateCommand(DomainManagementService service, Request request) {
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
			@SuppressWarnings("unchecked")
			List<String> fieldsToUpdate = (List<String>) this.request.getParameter("fieldsToUpdate");

			if (!this.service.machineConfigExists(machineId)) {
				ServerException exception = new ServerException("404", "Machine is not found.");
				Response response = new Response(Response.FAILURE, "Machine does not exist.", exception);
				responses.setResponse(response);
				return new CommandResultImpl(response);
			}

			MachineConfig updateMachineRequest = new MachineConfig();
			updateMachineRequest.setId(machineId);
			updateMachineRequest.setName(name);
			updateMachineRequest.setIpAddress(ipAddress);

			succeed = this.service.updateMachineConfig(updateMachineRequest, fieldsToUpdate);

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResultImpl(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Machine config is updated.");
		} else {
			response = new Response(Response.FAILURE, "Machine config is not updated.");
		}
		responses.setResponse(response);

		return new CommandResultImpl(response);
	}

}
