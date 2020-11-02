package other.orbit.component.runtime.tier3.domainmanagement.command;

import org.orbit.component.model.tier3.domain.PlatformConfigDTO;
import org.orbit.component.runtime.model.domain.PlatformConfig;
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

public class PlatformConfigGetCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public PlatformConfigGetCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		PlatformConfigDTO taConfigDTO = null;
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String id = (String) this.request.getParameter("id");

			PlatformConfig taConfig = this.service.getPlatformConfig(machineId, id);
			if (taConfig != null) {
				taConfigDTO = RuntimeModelConverter.Domain.toPlatformConfigDTO(taConfig);
				taConfigDTO.setMachineId(machineId);
			}

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResultImpl(response);
		}

		Response response = null;
		if (taConfigDTO != null) {
			response = new Response(Response.SUCCESS, "Transfer agent config is retrieved.");
			response.setBody(taConfigDTO);
		} else {
			response = new Response(Response.FAILURE, "Transfer agent config is not retrieved.");
		}
		responses.setResponse("response", response);

		return new CommandResultImpl(response);
	}

}
