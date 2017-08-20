package org.orbit.component.connector.tier3.transferagent;

import org.orbit.component.api.tier3.transferagent.NodespaceInfo;

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
