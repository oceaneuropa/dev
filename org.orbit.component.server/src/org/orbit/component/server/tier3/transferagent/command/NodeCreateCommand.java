package org.orbit.component.server.tier3.transferagent.command;

import javax.ws.rs.core.Response;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class NodeCreateCommand implements WSCommand {

	protected TransferAgentService service;

	public NodeCreateCommand(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		return null;
	}

}
