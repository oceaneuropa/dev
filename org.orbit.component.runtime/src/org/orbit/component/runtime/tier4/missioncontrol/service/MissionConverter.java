package org.orbit.component.runtime.tier4.missioncontrol.service;

import org.orbit.component.model.tier4.mission.MissionRTO;
import org.orbit.component.model.tier4.mission.MissionDTO;
import org.orbit.component.model.tier4.mission.MissionRTOImpl;
import org.orbit.component.model.tier4.mission.MissionVO;

public class MissionConverter {

	public static MissionConverter INSTANCE = new MissionConverter();

	public MissionRTO toMission(MissionVO missionVO) {
		MissionRTOImpl mission = new MissionRTOImpl();
		mission.setTypeId(missionVO.getTypeId());
		mission.setName(missionVO.getName());
		return mission;
	}

	public MissionDTO toDTO(MissionRTO mission) {
		MissionDTO dto = new MissionDTO();
		dto.setTypeId(mission.getTypeId());
		dto.setName(mission.getName());
		return dto;
	}

}
