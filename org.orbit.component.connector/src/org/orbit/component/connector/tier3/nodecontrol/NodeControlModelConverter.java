package org.orbit.component.connector.tier3.nodecontrol;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.model.tier3.nodecontrol.NodeDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class NodeControlModelConverter {

	public static NodeControlModelConverter INSTANCE = new NodeControlModelConverter();

	public NodeInfo toNode(NodeDTO nodeDTO) {
		NodeInfoImpl nodeInfo = new NodeInfoImpl();
		nodeInfo.setId(nodeDTO.getId());
		nodeInfo.setName(nodeDTO.getName());
		nodeInfo.setUri(nodeDTO.getUri());
		nodeInfo.setAttributes(nodeDTO.getAttributes());
		return nodeInfo;
	}

	public NodeInfo[] getNodes(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		List<NodeInfo> nodes = new ArrayList<NodeInfo>();
		List<NodeDTO> nodeDTOs = response.readEntity(new GenericType<List<NodeDTO>>() {
		});
		for (NodeDTO nodeDTO : nodeDTOs) {
			NodeInfo node = toNode(nodeDTO);
			nodes.add(node);
		}
		return nodes.toArray(new NodeInfo[nodes.size()]);
	}

	public NodeInfo getNode(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		NodeInfo node = null;
		NodeDTO nodeDTO = response.readEntity(NodeDTO.class);
		if (nodeDTO != null) {
			node = toNode(nodeDTO);
		}
		return node;
	}

	public boolean exists(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		boolean exists = false;
		try {
			exists = ResponseUtil.getSimpleValue(response, "exists", Boolean.class);
		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return exists;
	}

	public boolean isCreated(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isUpdated(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isDeleted(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isStarted(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isStopped(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isSucceed(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		boolean succeed = false;
		try {
			succeed = ResponseUtil.getSimpleValue(response, "succeed", Boolean.class);

		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return succeed;
	}

	public String getStatus(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		String status = null;
		try {
			status = ResponseUtil.getSimpleValue(response, "status", String.class);

		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return status;
	}

}

// public NodespaceInfo[] convertToNodespaceInfos(Responses responses) {
// List<NodespaceInfo> nodespaceInfos = new ArrayList<NodespaceInfo>();
// Response response = responses.getResponse(Response.class);
// if (response != null) {
// Object bodyObj = response.getBody();
// if (bodyObj instanceof List) {
// List<?> elements = (List<?>) bodyObj;
// for (Object element : elements) {
// if (element instanceof Map<?, ?>) {
// Map<?, ?> map = (Map<?, ?>) element;
// NodespaceInfo nodespaceInfo = TransferAgentConverter.INSTANCE.toNodespaceInfo(map);
// if (nodespaceInfo != null) {
// nodespaceInfos.add(nodespaceInfo);
// }
// }
// }
// }
// }
// return nodespaceInfos.toArray(new NodespaceInfo[nodespaceInfos.size()]);
// }

// public NodespaceInfo convertToNodespaceInfo(Responses responses) {
// NodespaceInfo nodespaceInfo = null;
// Response response = responses.getResponse(Response.class);
// if (response != null) {
// Object bodyObj = response.getBody();
// if (bodyObj instanceof Map<?, ?>) {
// Map<?, ?> map = (Map<?, ?>) bodyObj;
// nodespaceInfo = TransferAgentConverter.INSTANCE.toNodespaceInfo(map);
// }
// }
// return nodespaceInfo;
// }

// public NodeInfo[] convertToNodeInfos(Responses responses) {
// List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
// Response response = responses.getResponse(Response.class);
// if (response != null) {
// Object bodyObj = response.getBody();
// if (bodyObj instanceof List) {
// List<?> elements = (List<?>) bodyObj;
// for (Object element : elements) {
// if (element instanceof Map<?, ?>) {
// Map<?, ?> map = (Map<?, ?>) element;
// NodeInfo nodeInfo = TransferAgentConverter.INSTANCE.toNodeInfo(map);
// if (nodeInfo != null) {
// nodeInfos.add(nodeInfo);
// }
// }
// }
// }
// }
// return nodeInfos.toArray(new NodeInfo[nodeInfos.size()]);
// }

// public NodeInfo convertToNodeInfo(Responses responses) {
// NodeInfo nodeInfo = null;
// Response response = responses.getResponse(Response.class);
// if (response != null) {
// Object bodyObj = response.getBody();
// if (bodyObj instanceof Map<?, ?>) {
// Map<?, ?> map = (Map<?, ?>) bodyObj;
// nodeInfo = TransferAgentConverter.INSTANCE.toNodeInfo(map);
// }
// }
// return nodeInfo;
// }

/// **
// *
// * @param map
// * @return
// */
// public NodespaceInfo toNodespaceInfo(Map<?, ?> map) {
// String name = (String) map.get("name");
//
// NodespaceInfoImpl impl = new NodespaceInfoImpl();
// impl.setName(name);
// return impl;
// }

/// **
// *
// * @param map
// * @return
// */
// public NodeInfo toNodeInfo(Map<?, ?> map) {
// String id = (String) map.get("id");
// String name = (String) map.get("name");
//
// NodeInfoImpl impl = new NodeInfoImpl();
// impl.setId(id);
// impl.setName(name);
// return impl;
// }
