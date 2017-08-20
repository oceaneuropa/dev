package org.orbit.component.connector.tier3.transferagent;

import org.orbit.component.api.tier3.transferagent.NodeInfo;

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
