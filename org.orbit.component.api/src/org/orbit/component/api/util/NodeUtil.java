package org.orbit.component.api.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClientResolver;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;

public class NodeUtil {

	protected static NodeInfo[] EMPTY_NODE_INFOS = new NodeInfo[0];

	/**
	 * 
	 * @param nodeControlUrl
	 * @param accessToken
	 * @return
	 */
	public static NodeControlClient getClient(String nodeControlUrl, String accessToken) {
		NodeControlClient nodeControlClient = null;
		if (nodeControlUrl != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(WSClientConstants.REALM, null);
			properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
			properties.put(WSClientConstants.URL, nodeControlUrl);

			nodeControlClient = ComponentClients.getInstance().getNodeControl(properties);
		}
		return nodeControlClient;
	}

	/**
	 * 
	 * @param nodeControlClientResolver
	 * @param accessToken
	 * @param parentPlatformId
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public static NodeInfo[] getNodes(NodeControlClientResolver nodeControlClientResolver, String accessToken, String parentPlatformId) throws ClientException, IOException {
		NodeInfo[] nodeInfos = null;
		NodeControlClient nodeControlClient = nodeControlClientResolver.resolve(accessToken, parentPlatformId);
		if (nodeControlClient != null) {
			nodeInfos = nodeControlClient.getNodes();
		}
		if (nodeInfos == null) {
			nodeInfos = EMPTY_NODE_INFOS;
		}
		return nodeInfos;
	}

	/**
	 * 
	 * @param nodeControlClientResolver
	 * @param accessToken
	 * @param parentPlatformId
	 * @param nodeId
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public static NodeInfo getNode(NodeControlClientResolver nodeControlClientResolver, String accessToken, String parentPlatformId, String nodeId) throws ClientException, IOException {
		NodeInfo nodeInfo = null;
		NodeControlClient nodeControlClient = nodeControlClientResolver.resolve(accessToken, parentPlatformId);
		if (nodeControlClient != null && nodeId != null) {
			nodeInfo = nodeControlClient.getNode(nodeId);
		}
		return nodeInfo;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @param id
	 * @param name
	 * @param typeId
	 * @return
	 * @throws ClientException
	 */
	public static boolean createNode(NodeControlClient nodeControlClient, String id, String name, String typeId) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && id != null) {
			succeed = nodeControlClient.createNode(id, name, typeId);
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @param id
	 * @param name
	 * @param typeId
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateNode(NodeControlClient nodeControlClient, String id, String name, String typeId) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && id != null) {
			succeed = nodeControlClient.updateNode(id, name, typeId);
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @param nodeId
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.deleteNode(nodeId);
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @param nodeId
	 * @param options
	 * @return
	 * @throws ClientException
	 */
	public static boolean startNode(NodeControlClient nodeControlClient, String nodeId, Map<String, Object> options) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.startNode(nodeId, options);
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClientResolver
	 * @param accessToken
	 * @param parentPlatformId
	 * @param nodeId
	 * @param options
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public static boolean startNode(NodeControlClientResolver nodeControlClientResolver, String accessToken, String parentPlatformId, String nodeId, Map<String, Object> options) throws ClientException, IOException {
		boolean succeed = false;
		NodeControlClient nodeControlClient = nodeControlClientResolver.resolve(accessToken, parentPlatformId);
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.startNode(nodeId, options);
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @param nodeId
	 * @param options
	 * @return
	 * @throws ClientException
	 */
	public static boolean stopNode(NodeControlClient nodeControlClient, String nodeId, Map<String, Object> options) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.stopNode(nodeId, options);
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @param nodeId
	 * @param name
	 * @param value
	 * @return
	 * @throws ClientException
	 */
	public static boolean addNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String name, Object value) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.addNodeAttribute(nodeId, name, value);
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @param nodeId
	 * @param oldName
	 * @param name
	 * @param value
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String oldName, String name, Object value) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.updateNodeAttribute(nodeId, oldName, name, value);
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @param nodeId
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String name) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.deleteNodeAttribute(nodeId, name);
		}
		return succeed;
	}

}
