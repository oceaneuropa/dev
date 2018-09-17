package org.orbit.spirit.runtime.gaia.ws.command;

import org.orbit.spirit.model.RequestConstants;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.origin.common.rest.editpolicy.AbstractServiceEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class GaiaEditPolicy extends AbstractServiceEditPolicy {

	public static final String ID = "spirit.gaia.editpolicy";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public WSCommand getCommand(Request request) {
		GaiaService gaia = getService(GaiaService.class);
		if (gaia == null) {
			return null;
		}

		String requestName = request.getRequestName();

		if (RequestConstants.LIST_WORLDS.equals(requestName)) {
			return new WorldListWSCommand(gaia);

		} else if (RequestConstants.GET_WORLD.equals(requestName)) {
			return new WorldGetWSCommand(gaia);

		} else if (RequestConstants.WORLD_EXIST.equals(requestName)) {
			return new WorldExistWSCommand(gaia);

		} else if (RequestConstants.CREATE_WORLD.equals(requestName)) {
			return new WorldCreateWSCommand(gaia);

		} else if (RequestConstants.DELETE_WORLD.equals(requestName)) {
			return new WorldDeleteWSCommand(gaia);

		} else if (RequestConstants.WORLD_STATUS.equals(requestName)) {
			return new WorldStatusWSCommand(gaia);
		}

		return null;
	}

}
