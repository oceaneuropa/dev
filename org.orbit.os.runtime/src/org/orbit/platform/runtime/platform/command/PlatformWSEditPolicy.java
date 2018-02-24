package org.orbit.platform.runtime.platform.command;

import org.orbit.platform.runtime.RequestConstants;
import org.orbit.platform.runtime.platform.Platform;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class PlatformWSEditPolicy extends AbstractWSEditPolicy {

	public static final String ID = "platform.editpolicy";

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

		if (RequestConstants.START_SERVICE.equals(requestName)) {
			return new StartServiceCommand(platform);

		} else if (RequestConstants.STOP_SERVICE.equals(requestName)) {
			return new StopServiceCommand(platform);
		}

		return null;
	}

}
