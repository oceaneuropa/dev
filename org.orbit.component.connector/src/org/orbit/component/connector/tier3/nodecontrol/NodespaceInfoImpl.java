package org.orbit.component.connector.tier3.nodecontrol;

import org.orbit.component.api.tier3.nodecontrol.NodespaceInfo;

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
