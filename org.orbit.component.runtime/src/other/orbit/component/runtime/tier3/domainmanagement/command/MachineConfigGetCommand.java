package other.orbit.component.runtime.tier3.domainmanagement.command;

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
	public CommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		MachineConfigDTO machineConfigDTO = null;
		try {
			String machineId = (String) this.request.getParameter("machineId");

			MachineConfig machineConfig = this.service.getMachineConfig(machineId);
			if (machineConfig != null) {
				machineConfigDTO = RuntimeModelConverter.Domain.toMachineConfigDTO(machineConfig);
			}

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResultImpl(response);
		}

		Response response = null;
		if (machineConfigDTO != null) {
			response = new Response(Response.SUCCESS, "Machine config is retrieved.");
			response.setBody(machineConfigDTO);
		} else {
			response = new Response(Response.FAILURE, "Machine config is not retrieved.");
		}
		responses.setResponse("response", response);

		return new CommandResultImpl(response);
	}

}
