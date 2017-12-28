package org.orbit.os.runtime.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.os.runtime.service.GAIA;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;

public class WorldExistWSCommand extends AbstractWSCommand {

	protected GAIA gaia;

	public WorldExistWSCommand(GAIA gaia) {
		this.gaia = gaia;
	}

	@Override
	public Response execute(Request request) {
		return null;
	}

}