package other.orbit.component.runtime.tier3.domainmanagement.command;

import java.util.List;

import org.orbit.component.runtime.model.domain.PlatformConfig;
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

public class PlatformConfigUpdateCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public PlatformConfigUpdateCommand(DomainManagementService service, Request request) {
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
			String name = (String) this.request.getParameter("name");
			String home = (String) this.request.getParameter("home");
			String hostURL = (String) this.request.getParameter("hostURL");
			String contextRoot = (String) this.request.getParameter("contextRoot");

			@SuppressWarnings("unchecked")
			List<String> fieldsToUpdate = (List<String>) this.request.getParameter("fieldsToUpdate");

			if (!this.service.platformConfigExists(machineId, id)) {
				ServerException exception = new ServerException("404", "Transfer Agent is not found.");
				Response response = new Response(Response.FAILURE, "Transfer Agent does not exist.", exception);
				responses.setResponse(response);
				return new CommandResultImpl(response);
			}

			PlatformConfig updateTaRequest = new PlatformConfig();
			updateTaRequest.setId(id);
			updateTaRequest.setName(name);
			updateTaRequest.setHome(home);
			updateTaRequest.setHostURL(hostURL);
			updateTaRequest.setContextRoot(contextRoot);

			succeed = this.service.updatePlatformConfig(machineId, updateTaRequest, fieldsToUpdate);

		} catch (ServerException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResultImpl(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Transfer Agent config is updated.");
		} else {
			response = new Response(Response.FAILURE, "Transfer Agent config is not updated.");
		}
		responses.setResponse(response);

		return new CommandResultImpl(response);
	}

}
