package org.orbit.component.model.tier4.mission.dto;

public class MissionImpl implements Mission {

	protected String typeId;
	protected String name;

	public MissionImpl() {
	}

	public MissionImpl(String typeId, String name) {
		this.typeId = typeId;
		this.name = name;
	}

	@Override
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
