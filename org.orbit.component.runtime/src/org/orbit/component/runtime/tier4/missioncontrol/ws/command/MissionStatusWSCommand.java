package org.orbit.component.runtime.tier4.missioncontrol.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class MissionStatusWSCommand extends AbstractWSCommand {

	protected MissionControlService service;

	public MissionStatusWSCommand(MissionControlService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		return Response.status(Status.OK).build();
	}

}
