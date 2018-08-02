package org.orbit.component.connector.tier4.mission;

import org.orbit.component.api.tier4.missioncontrol.Mission;

public class MissionImpl implements Mission {

	protected String typeId;
	protected String name;

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
