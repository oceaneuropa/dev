package other.orbit.component.runtime.tier3.domainmanagement.command;

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

public class PlatformConfigRemoveCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public PlatformConfigRemoveCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		boolean succeed = false;
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String id = (String) this.request.getParameter("id");

			if (!this.service.platformConfigExists(machineId, id)) {
				ServerException exception = new ServerException("404", "Transfer agent is not found.");
				Response response = new Response(Response.FAILURE, "Transfer agent does not exist.", exception);
				responses.setResponse(response);
				return new CommandResultImpl(response);
			}

			succeed = this.service.deletePlatformConfig(machineId, id);

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResultImpl(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Transfer agent config is removed.");
		} else {
			response = new Response(Response.FAILURE, "Transfer agent config is not removed.");
		}
		responses.setResponse(response);

		return new CommandResultImpl(response);
	}

}
