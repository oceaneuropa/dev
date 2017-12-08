package org.orbit.component.model.tier3.transferagent.dto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class TransferAgentResponseConverter {

	public static TransferAgentResponseConverter INSTANCE = new TransferAgentResponseConverter();

	public NodeInfo[] convertToNodeInfos(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}

		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
		List<INodeDTO> nodeDTOs = response.readEntity(new GenericType<List<INodeDTO>>() {
		});
		for (INodeDTO nodeDTO : nodeDTOs) {
			NodeInfoImpl nodeInfo = new NodeInfoImpl();
			nodeInfo.setId(nodeDTO.getId());
			nodeInfo.setName(nodeDTO.getName());
			nodeInfos.add(nodeInfo);
		}
		return nodeInfos.toArray(new NodeInfo[nodeInfos.size()]);
	}

	public NodeInfo convertToNodeInfo(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}

		NodeInfoImpl nodeInfo = null;
		INodeDTO nodeDTO = response.readEntity(INodeDTO.class);
		if (nodeDTO != null) {
			nodeInfo = new NodeInfoImpl();
			nodeInfo.setId(nodeDTO.getId());
			nodeInfo.setName(nodeDTO.getName());
		}
		return nodeInfo;
	}

	public boolean nodeExists(Response response) throws ClientException {
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

	public boolean createNodeSucceed(Response response) throws ClientException {
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

	public boolean deleteNodeSucceed(Response response) throws ClientException {
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

	public String getNodeStatus(Response response) throws ClientException {
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
