package org.orbit.component.runtime.tier4.missioncontrol.ws.command;

import org.orbit.component.model.RequestConstants;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.origin.common.rest.editpolicy.AbstractServiceEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class MissionControlEditPolicy extends AbstractServiceEditPolicy {

	public static final String ID = "component.mission_control.editpolicy";

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

		if (RequestConstants.GET_MISSIONS.equals(requestName)) {
			return new MissionListWSCommand(service);

		} else if (RequestConstants.GET_MISSION.equals(requestName)) {
			return new MissionGetWSCommand(service);

		} else if (RequestConstants.MISSION_EXIST.equals(requestName)) {
			return new MissionExistWSCommand(service);

		} else if (RequestConstants.CREATE_MISSION.equals(requestName)) {
			return new MissionCreateWSCommand(service);

		} else if (RequestConstants.DELETE_MISSION.equals(requestName)) {
			return new MissionDeleteWSCommand(service);

		} else if (RequestConstants.MISSION_STATUS.equals(requestName)) {
			return new MissionStatusWSCommand(service);
		}

		return null;
	}

}
