package org.orbit.component.model.tier3.transferagent.dto;

public class NodespaceInfoImpl implements NodespaceInfo {

	protected String name;

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
