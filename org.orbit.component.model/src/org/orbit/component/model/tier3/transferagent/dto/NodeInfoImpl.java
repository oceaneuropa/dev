package org.orbit.component.model.tier3.transferagent.dto;

public class NodeInfoImpl implements NodeInfo {

	protected String id;
	protected String name;

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
