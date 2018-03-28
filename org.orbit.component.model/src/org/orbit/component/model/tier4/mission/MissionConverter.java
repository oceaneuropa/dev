package org.orbit.component.model.tier4.mission;

import org.orbit.component.model.tier4.mission.dto.MissionDTO;

public class MissionConverter {

	public static MissionConverter INSTANCE = new MissionConverter();

	public Mission toMission(MissionVO missionVO) {
		MissionImpl mission = new MissionImpl();
		mission.setTypeId(missionVO.getTypeId());
		mission.setName(missionVO.getName());
		return mission;
	}

	public MissionDTO toDTO(Mission mission) {
		MissionDTO dto = new MissionDTO();
		dto.setTypeId(mission.getTypeId());
		dto.setName(mission.getName());
		return dto;
	}

}
