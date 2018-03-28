package org.orbit.component.connector.tier4.mission;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.component.model.tier4.mission.MissionDTO;
import org.orbit.component.model.tier4.mission.MissionRTO;
import org.orbit.component.model.tier4.mission.MissionRTOImpl;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class MissionControlModelConverter {

	public static MissionControlModelConverter INSTANCE = new MissionControlModelConverter();

	public MissionRTO toMission(MissionDTO missionDTO) {
		MissionRTOImpl mission = new MissionRTOImpl();
		mission.setTypeId(missionDTO.getTypeId());
		mission.setName(missionDTO.getName());
		return mission;
	}

	public MissionRTO[] getMissions(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}

		List<MissionRTO> missions = new ArrayList<MissionRTO>();
		List<MissionDTO> missionDTOs = response.readEntity(new GenericType<List<MissionDTO>>() {
		});
		for (MissionDTO missionDTO : missionDTOs) {
			MissionRTO mission = toMission(missionDTO);
			missions.add(mission);
		}
		return missions.toArray(new MissionRTO[missions.size()]);
	}

	public MissionRTO getMission(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}

		MissionRTO mission = null;
		MissionDTO missionDTO = response.readEntity(MissionDTO.class);
		if (missionDTO != null) {
			mission = toMission(missionDTO);
		}
		return mission;
	}

	public boolean exists(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		boolean exists = false;
		try {
			exists = ResponseUtil.getSimpleValue(response, "exists", Boolean.class);
		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return exists;
	}

	public boolean isCreated(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isDeleted(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isSucceed(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		boolean succeed = false;
		try {
			succeed = ResponseUtil.getSimpleValue(response, "succeed", Boolean.class);

		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return succeed;
	}

}
