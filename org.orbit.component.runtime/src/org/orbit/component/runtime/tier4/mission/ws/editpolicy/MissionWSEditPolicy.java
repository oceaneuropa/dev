package org.orbit.component.runtime.tier4.mission.ws.editpolicy;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier4.mission.service.MissionControlService;
import org.orbit.component.runtime.tier4.mission.ws.command.MissionCreateWSCommand;
import org.orbit.component.runtime.tier4.mission.ws.command.MissionDeleteWSCommand;
import org.orbit.component.runtime.tier4.mission.ws.command.MissionExistWSCommand;
import org.orbit.component.runtime.tier4.mission.ws.command.MissionGetWSCommand;
import org.orbit.component.runtime.tier4.mission.ws.command.MissionListWSCommand;
import org.orbit.component.runtime.tier4.mission.ws.command.MissionStatusWSCommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class MissionWSEditPolicy extends AbstractWSEditPolicy {

	public static final String ID = "mission_control.mission.editpolicy";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public WSCommand getCommand(Request request) {
		MissionControlService service = getService(MissionControlService.class);
		if (service == null) {
			return null;
		}

		String requestName = request.getRequestName();

		if (Requests.GET_MISSIONS.equals(requestName)) {
			return new MissionListWSCommand(service);

		} else if (Requests.GET_MISSION.equals(requestName)) {
			return new MissionGetWSCommand(service);

		} else if (Requests.MISSION_EXIST.equals(requestName)) {
			return new MissionExistWSCommand(service);

		} else if (Requests.CREATE_MISSION.equals(requestName)) {
			return new MissionCreateWSCommand(service);

		} else if (Requests.DELETE_MISSION.equals(requestName)) {
			return new MissionDeleteWSCommand(service);

		} else if (Requests.MISSION_STATUS.equals(requestName)) {
			return new MissionStatusWSCommand(service);
		}

		return null;
	}

}
