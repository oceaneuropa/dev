package org.orbit.platform.runtime.core.command;

import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.core.Platform;
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

		if (PlatformConstants.START_SERVICE.equals(requestName)) {
			return new StartServiceCommand(platform);

		} else if (PlatformConstants.STOP_SERVICE.equals(requestName)) {
			return new StopServiceCommand(platform);

		} else if (PlatformConstants.SHUTDOWN_PLATFORM.equals(requestName)) {
			return new ShutdownPlatformCommand(platform);
		}

		return null;
	}

}
