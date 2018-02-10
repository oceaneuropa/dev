package org.orbit.platform.runtime.gaia.ws.command;

import org.orbit.platform.runtime.Requests;
import org.orbit.platform.runtime.gaia.service.GAIA;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class GaiaWSEditPolicy extends AbstractWSEditPolicy {

	public static final String ID = "gaia.editpolicy";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public WSCommand getCommand(Request request) {
		GAIA gaia = getService(GAIA.class);
		if (gaia == null) {
			return null;
		}

		String requestName = request.getRequestName();

		if (Requests.LIST_WORLDS.equals(requestName)) {
			return new WorldListWSCommand(gaia);

		} else if (Requests.GET_WORLD.equals(requestName)) {
			return new WorldGetWSCommand(gaia);

		} else if (Requests.WORLD_EXIST.equals(requestName)) {
			return new WorldExistWSCommand(gaia);

		} else if (Requests.CREATE_WORLD.equals(requestName)) {
			return new WorldCreateWSCommand(gaia);

		} else if (Requests.DELETE_WORLD.equals(requestName)) {
			return new WorldDeleteWSCommand(gaia);

		} else if (Requests.WORLD_STATUS.equals(requestName)) {
			return new WorldStatusWSCommand(gaia);
		}

		return null;
	}

}
