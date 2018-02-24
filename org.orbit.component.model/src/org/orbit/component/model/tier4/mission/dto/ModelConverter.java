package org.orbit.component.model.tier4.mission.dto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class ModelConverter {

	public static ModelConverter INSTANCE = new ModelConverter();

	public Mission toMission(MissionDTO missionDTO) {
		MissionImpl mission = new MissionImpl();
		mission.setTypeId(missionDTO.getTypeId());
		mission.setName(missionDTO.getName());
		return mission;
	}

	public Mission[] getMissions(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}

		List<Mission> missions = new ArrayList<Mission>();
		List<MissionDTO> missionDTOs = response.readEntity(new GenericType<List<MissionDTO>>() {
		});
		for (MissionDTO missionDTO : missionDTOs) {
			Mission mission = toMission(missionDTO);
			missions.add(mission);
		}
		return missions.toArray(new Mission[missions.size()]);
	}

	public Mission getMission(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}

		Mission mission = null;
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
