package org.orbit.component.runtime.tier4.missioncontrol.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier4.missioncontrol.MissionDTO;
import org.orbit.component.runtime.model.missioncontrol.Mission;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionConverter;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.ServerException;

public class MissionListWSCommand extends AbstractWSCommand {

	protected MissionControlService service;

	public MissionListWSCommand(MissionControlService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		String typeId = (request.getParameter("typeId") instanceof String) ? (String) request.getParameter("typeId") : null;
		if (typeId == null || typeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'typeId' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		List<MissionDTO> missionDTOs = new ArrayList<MissionDTO>();
		try {
			List<Mission> missions = this.service.getMissions(typeId);
			for (Mission mission : missions) {
				MissionDTO missionDTO = MissionConverter.INSTANCE.toDTO(mission);
				missionDTOs.add(missionDTO);
			}

		} catch (ServerException e) {
			e.printStackTrace();

			ErrorDTO error = handleError(e, "500", true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.status(Status.OK).entity(missionDTOs).build();
	}

}
