package org.orbit.component.connector.tier3.transferagent;

import java.util.Map;

import org.orbit.component.api.tier3.transferagent.NodeInfo;
import org.orbit.component.api.tier3.transferagent.NodespaceInfo;

public class TransferAgentConverter {

	public static TransferAgentConverter INSTANCE = new TransferAgentConverter();

	/**
	 * 
	 * @param map
	 * @return
	 */
	public NodespaceInfo toNodespaceInfo(Map<?, ?> map) {
		String name = (String) map.get("name");

		NodespaceInfoImpl impl = new NodespaceInfoImpl();
		impl.setName(name);
		return impl;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public NodeInfo toNodeInfo(Map<?, ?> map) {
		String id = (String) map.get("id");
		String name = (String) map.get("name");

		NodeInfoImpl impl = new NodeInfoImpl();
		impl.setId(id);
		impl.setName(name);
		return impl;
	}

}
