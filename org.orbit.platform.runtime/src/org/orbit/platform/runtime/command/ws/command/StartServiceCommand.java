package org.orbit.platform.runtime.command.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.platform.runtime.command.service.CommandService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class StartServiceCommand extends AbstractWSCommand {

	protected CommandService service;

	/**
	 * 
	 * @param service
	 */
	public StartServiceCommand(CommandService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) throws Exception {
		request.getParameter("extensionTypeId");
		request.getParameter("extensionId");

		// Map<String, Object> parameters = request.getParameters();

		return null;
	}

}
