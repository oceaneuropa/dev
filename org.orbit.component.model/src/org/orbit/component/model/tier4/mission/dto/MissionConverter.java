package org.orbit.component.model.tier4.mission.dto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class MissionConverter {

	public static MissionConverter INSTANCE = new MissionConverter();

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

	public Mission toMission(MissionDTO missionDTO) {
		MissionImpl mission = new MissionImpl();
		mission.setTypeId(missionDTO.getTypeId());
		mission.setName(missionDTO.getName());
		return mission;
	}

}
