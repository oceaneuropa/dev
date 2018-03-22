package org.orbit.component.runtime.tier3.nodemanagement.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class NodeStatusWSCommand extends AbstractWSCommand {

	protected NodeManagementService service;

	public NodeStatusWSCommand(NodeManagementService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		return Response.status(Status.OK).build();
	}

}
