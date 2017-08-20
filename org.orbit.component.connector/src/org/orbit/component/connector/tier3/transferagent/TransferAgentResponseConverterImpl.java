package org.orbit.component.connector.tier3.transferagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.transferagent.NodeInfo;
import org.orbit.component.api.tier3.transferagent.NodespaceInfo;
import org.orbit.component.api.tier3.transferagent.TransferAgentResponseConverter;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class TransferAgentResponseConverterImpl implements TransferAgentResponseConverter {

	@Override
	public NodespaceInfo[] convertToNodespaceInfos(Responses responses) {
		List<NodespaceInfo> nodespaceInfos = new ArrayList<NodespaceInfo>();
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof List) {
				List<?> elements = (List<?>) bodyObj;
				for (Object element : elements) {
					if (element instanceof Map<?, ?>) {
						Map<?, ?> map = (Map<?, ?>) element;
						NodespaceInfo nodespaceInfo = TransferAgentConverter.INSTANCE.toNodespaceInfo(map);
						if (nodespaceInfo != null) {
							nodespaceInfos.add(nodespaceInfo);
						}
					}
				}
			}
		}
		return nodespaceInfos.toArray(new NodespaceInfo[nodespaceInfos.size()]);
	}

	@Override
	public NodespaceInfo convertToNodespaceInfo(Responses responses) {
		NodespaceInfo nodespaceInfo = null;
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) bodyObj;
				nodespaceInfo = TransferAgentConverter.INSTANCE.toNodespaceInfo(map);
			}
		}
		return nodespaceInfo;
	}

	@Override
	public NodeInfo[] convertToNodeInfos(Responses responses) {
		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof List) {
				List<?> elements = (List<?>) bodyObj;
				for (Object element : elements) {
					if (element instanceof Map<?, ?>) {
						Map<?, ?> map = (Map<?, ?>) element;
						NodeInfo nodeInfo = TransferAgentConverter.INSTANCE.toNodeInfo(map);
						if (nodeInfo != null) {
							nodeInfos.add(nodeInfo);
						}
					}
				}
			}
		}
		return nodeInfos.toArray(new NodeInfo[nodeInfos.size()]);
	}

	@Override
	public NodeInfo convertToNodeInfo(Responses responses) {
		NodeInfo nodeInfo = null;
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) bodyObj;
				nodeInfo = TransferAgentConverter.INSTANCE.toNodeInfo(map);
			}
		}
		return nodeInfo;
	}

}
