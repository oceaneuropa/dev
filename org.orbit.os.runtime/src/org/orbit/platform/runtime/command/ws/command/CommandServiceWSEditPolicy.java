package org.orbit.platform.runtime.command.ws.command;

import org.orbit.platform.runtime.RequestConstants;
import org.orbit.platform.runtime.command.service.CommandService;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class CommandServiceWSEditPolicy extends AbstractWSEditPolicy {

	public static final String ID = "os.command_service.editpolicy";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public WSCommand getCommand(Request request) {
		CommandService commandService = getService(CommandService.class);
		if (commandService == null) {
			return null;
		}

		String requestName = request.getRequestName();

		if (RequestConstants.START_SERVICE.equals(requestName)) {
			return new StartServiceCommand(commandService);

		} else if (RequestConstants.STOP_SERVICE.equals(requestName)) {
			return new StopServiceCommand(commandService);
		}

		return null;
	}

}