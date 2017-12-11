package org.orbit.component.runtime.tier3.transferagent.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class NodeStatusWSCommand extends AbstractWSCommand {

	protected TransferAgentService service;

	public NodeStatusWSCommand(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		return Response.status(Status.OK).build();
	}

}
