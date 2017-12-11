package org.orbit.component.runtime.tier3.domain.command;

import java.util.List;

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class NodeConfigUpdateCommand extends AbstractCommand {

	protected DomainManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public NodeConfigUpdateCommand(DomainManagementService service, Request request) {
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

			@SuppressWarnings("unchecked")
			List<String> fieldsToUpdate = (List<String>) this.request.getParameter("fieldsToUpdate");

			if (!this.service.nodeConfigExists(machineId, transferAgentId, id)) {
				DomainMgmtException exception = new DomainMgmtException("404", "Node config is not found.");
				Response response = new Response(Response.FAILURE, "Node config does not exist.", exception);
				responses.setResponse(response);
				return new CommandResult(response);
			}

			NodeConfigRTO updateNodeRequest = new NodeConfigRTO();
			updateNodeRequest.setMachineId(machineId);
			updateNodeRequest.setTransferAgentId(transferAgentId);
			updateNodeRequest.setId(id);
			updateNodeRequest.setName(name);
			updateNodeRequest.setHome(home);
			updateNodeRequest.setHostURL(hostURL);
			updateNodeRequest.setContextRoot(contextRoot);

			succeed = this.service.updateNodeConfig(machineId, transferAgentId, updateNodeRequest, fieldsToUpdate);

		} catch (DomainMgmtException e) {
			Response response = new Response(Response.EXCEPTION, e.getMessage(), e);
			responses.setResponse(response);
			return new CommandResult(response);
		}

		Response response = null;
		if (succeed) {
			response = new Response(Response.SUCCESS, "Node config is updated.");
		} else {
			response = new Response(Response.FAILURE, "Node config is not updated.");
		}
		responses.setResponse(response);

		return new CommandResult(response);
	}

}
