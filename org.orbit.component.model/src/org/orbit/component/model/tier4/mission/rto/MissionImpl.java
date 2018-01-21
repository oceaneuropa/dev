package org.orbit.component.model.tier4.mission.rto;

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
