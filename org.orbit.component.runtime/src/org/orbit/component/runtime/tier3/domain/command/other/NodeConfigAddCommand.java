package org.orbit.component.runtime.tier3.domain.command.other;

import org.orbit.component.model.tier3.domain.DomainException;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.runtime.tier3.domain.service.DomainService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class NodeConfigAddCommand extends AbstractCommand {

	protected DomainService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodeConfigAddCommand(DomainService service, Request request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		Responses responses = context.getAdapter(Responses.class);

		boolean succeed = false;
		try {
			String machineId = (String) this.request.getParameter("machineId");
			String transferAgentId = (String) this.request.getParameter("transferAgentId");

			String id = (String) this.request.getParameter("id");
			String name = (String) this.request.getParameter("name");
			String home = (String) this.request.getParameter("home");
			String hostURL = (String) this.request.getParameter("hostURL");
			String contextRoot = (String) this.request.getParameter("contextRoot");

			if (this.service.nodeConfigExists(machineId, transferAgentId, id)) {
				Response response = new Response(Response.FAILURE, "Node config already exists.");
				responses.setResponse(response);
				return new CommandResult(response);
			}

			NodeConfigRTO addNodeRequest = new NodeConfigRTO(id, machineId, transferAgentId, name, home, hostURL, contextRoot);
			succeed = this.service.addNodeConfig(machineId, transferAgentId, addNodeRequest);

		} catch (DomainException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse("response", response);
			return new CommandResult(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Node config is added.");
		} else {
			response = new Response(Response.FAILURE, "Node config is not added.");
		}
		responses.setResponse("response", response);

		return new CommandResult(response);
	}

}