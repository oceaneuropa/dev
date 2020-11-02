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

public class NodeConfigRemoveCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodeConfigRemoveCommand(DomainManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		boolean succeed = false;
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String platformId = (String) this.request.getParameter("platformId");
			String id = (String) this.request.getParameter("id");

			if (!this.service.nodeConfigExists(machineId, platformId, id)) {
				ServerException exception = new ServerException("404", "Node config is not found.");
				Response response = new Response(Response.FAILURE, "Node config does not exist.", exception);
				responses.setResponse(response);
				return new CommandResultImpl(response);
			}

			succeed = this.service.deleteNodeConfig(machineId, platformId, id);

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResultImpl(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Node config is removed.");
		} else {
			response = new Response(Response.FAILURE, "Node config is not removed.");
		}
		responses.setResponse(response);

		return new CommandResultImpl(response);
	}

}
