package org.orbit.platform.runtime.platform.command;

import org.orbit.platform.runtime.Requests;
import org.orbit.platform.runtime.gaia.service.GAIA;
import org.orbit.platform.runtime.gaia.ws.command.WorldCreateWSCommand;
import org.orbit.platform.runtime.gaia.ws.command.WorldDeleteWSCommand;
import org.orbit.platform.runtime.gaia.ws.command.WorldExistWSCommand;
import org.orbit.platform.runtime.gaia.ws.command.WorldGetWSCommand;
import org.orbit.platform.runtime.gaia.ws.command.WorldListWSCommand;
import org.orbit.platform.runtime.gaia.ws.command.WorldStatusWSCommand;
import org.orbit.platform.runtime.platform.Platform;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class PlatformWSEditPolicy extends AbstractWSEditPolicy {

	public static final String ID = "gaia.editpolicy";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public WSCommand getCommand(Request request) {
		Platform platform = getService(Platform.class);
		if (platform == null) {
			return null;
		}

		String requestName = request.getRequestName();

		// if (Requests.LIST_WORLDS.equals(requestName)) {
		// return new WorldListWSCommand(platform);
		//
		// } else if (Requests.GET_WORLD.equals(requestName)) {
		// return new WorldGetWSCommand(platform);
		//
		// } else if (Requests.WORLD_EXIST.equals(requestName)) {
		// return new WorldExistWSCommand(platform);
		//
		// } else if (Requests.CREATE_WORLD.equals(requestName)) {
		// return new WorldCreateWSCommand(platform);
		//
		// } else if (Requests.DELETE_WORLD.equals(requestName)) {
		// return new WorldDeleteWSCommand(platform);
		//
		// } else if (Requests.WORLD_STATUS.equals(requestName)) {
		// return new WorldStatusWSCommand(platform);
		// }

		return null;
	}

}