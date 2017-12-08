package org.orbit.os.server.ws.editpolicy;

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
		return null;
	}

}
