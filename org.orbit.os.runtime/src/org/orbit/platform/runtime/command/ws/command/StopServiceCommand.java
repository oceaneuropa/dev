package org.orbit.platform.runtime.command.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.platform.runtime.command.service.CommandService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class StopServiceCommand extends AbstractWSCommand {

	protected CommandService service;

	/**
	 * 
	 * @param service
	 */
	public StopServiceCommand(CommandService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) throws Exception {
		return null;
	}

}
