package org.orbit.component.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.api.tier4.missioncontrol.MissionControlClient;
import org.origin.common.rest.client.WSClientConstants;

public class OthersUtil {

	/**
	 * 
	 * @param authServiceUrl
	 * @param accessToken
	 * @return
	 */
	public static AuthClient getAuthClient(String authServiceUrl, String accessToken) {
		AuthClient auth = null;
		if (authServiceUrl != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(WSClientConstants.REALM, null);
			properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
			properties.put(WSClientConstants.URL, authServiceUrl);
			auth = ComponentClients.getInstance().getAuth(properties);
		}
		return auth;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @return
	 */
	public static MissionControlClient getMissionControlClient(String missionControlUrl, String accessToken) {
		MissionControlClient missionControl = null;
		if (missionControlUrl != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(WSClientConstants.REALM, null);
			properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
			properties.put(WSClientConstants.URL, missionControlUrl);

			missionControl = ComponentClients.getInstance().getMissionControl(properties);
		}
		return missionControl;
	}
}

// /**
// *
// * @param identityServiceUrl
// * @param username
// * @return
// * @throws ClientException
// */
// public boolean usernameExists(String identityServiceUrl, String username) throws ClientException {
// boolean exists = false;
// IdentityClient identityClient = getIdentityClient(identityServiceUrl, null);
// if (identityClient != null) {
// exists = identityClient.usernameExists(username);
// }
// return exists;
// }
//
// /**
// *
// * @param identityServiceUrl
// * @param username
// * @param email
// * @return
// * @throws ClientException
// */
// public boolean emailExists(String identityServiceUrl, String email) throws ClientException {
// boolean exists = false;
// IdentityClient identityClient = getIdentityClient(identityServiceUrl, null);
// if (identityClient != null) {
// exists = identityClient.emailExists(email);
// }
// return exists;
// }

// /**
// *
// * @param platformConfig
// * @return
// * @throws ClientException
// */
// protected NodeControlClient getNodeControlClient(PlatformConfig platformConfig) throws ClientException {
// NodeControlClient nodeControlClient = null;
// if (platformConfig != null) {
// String url = platformConfig.getHostURL() + platformConfig.getContextRoot();
// nodeControlClient = OrbitClients.getInstance().getNodeControl(url);
// }
// return nodeControlClient;
// }

// /**
// *
// * @param nodeIdToIndexItem
// * @param nodeId
// * @return
// */
// public PlatformClient getNodePlatformClient(Map<String, IndexItem> nodeIdToIndexItem, String nodeId) {
// PlatformClient nodePlatformClient = null;
// if (nodeIdToIndexItem != null && nodeId != null) {
// IndexItem nodeIndexItem = nodeIdToIndexItem.get(nodeId);
// if (nodeIndexItem != null) {
// nodePlatformClient = getNodePlatformClient(nodeIndexItem);
// }
// }
// return nodePlatformClient;
// }
//
// /**
// *
// * @param nodeIdToIndexItem
// * @param nodeId
// * @return
// */
// public PlatformClient getNodePlatformClient(IndexItem nodeIndexItem) {
// PlatformClient nodePlatformClient = null;
// if (nodeIndexItem != null) {
// String nodePlatformUrl = null;
// String platformHostUrl = (String) nodeIndexItem.getProperties().get(PlatformConstants.PLATFORM_HOST_URL);
// String platformContextRoot = (String) nodeIndexItem.getProperties().get(PlatformConstants.PLATFORM_CONTEXT_ROOT);
//
// if (platformHostUrl != null && platformContextRoot != null) {
// nodePlatformUrl = platformHostUrl;
// if (!nodePlatformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
// nodePlatformUrl += "/";
// }
// nodePlatformUrl += platformContextRoot;
// }
//
// if (nodePlatformUrl != null) {
// nodePlatformClient = Clients.getInstance().getPlatformClient(nodePlatformUrl);
// }
// }
// return nodePlatformClient;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @return
// * @throws ClientException
// */
// public NodeControlClient getNodeControlClient(String domainServiceUrl, String machineId, String platformId) throws ClientException {
// NodeControlClient nodeControlClient = null;
// if (domainServiceUrl != null && machineId != null && platformId != null) {
// DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
// if (domainClient != null) {
// PlatformConfig platformConfig = domainClient.getPlatformConfig(machineId, platformId);
// if (platformConfig != null) {
// String url = platformConfig.getHostURL() + platformConfig.getContextRoot();
// nodeControlClient = OrbitClients.getInstance().getNodeControl(url);
// }
// }
// }
// return nodeControlClient;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @return
// * @throws ClientException
// */
// public NodeInfo[] getNodes(String domainServiceUrl, String machineId, String platformId) throws ClientException {
// NodeInfo[] nodeInfos = null;
// if (domainServiceUrl != null && machineId != null && platformId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// nodeInfos = nodeControlClient.getNodes();
// }
// }
// return nodeInfos;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public NodeInfo getNode(String domainServiceUrl, String machineId, String platformId, String nodeId) throws ClientException {
// NodeInfo nodeInfo = null;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// nodeInfo = nodeControlClient.getNode(nodeId);
// }
// }
// return nodeInfo;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param id
// * @param name
// * @param typeId
// * @return
// * @throws ClientException
// */
// public boolean createNode(String domainServiceUrl, String machineId, String platformId, String id, String name, String typeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && id != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.createNode(id, name, typeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param id
// * @param name
// * @param typeId
// * @return
// * @throws ClientException
// */
// public boolean updateNode(String domainServiceUrl, String machineId, String platformId, String id, String name, String typeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && id != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.updateNode(id, name, typeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public boolean deleteNode(String domainServiceUrl, String machineId, String platformId, String nodeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.deleteNode(nodeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public boolean startNode(String domainServiceUrl, String machineId, String platformId, String nodeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.startNode(nodeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public boolean stopNode(String domainServiceUrl, String machineId, String platformId, String nodeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.stopNode(nodeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @param name
// * @param value
// * @return
// * @throws ClientException
// */
// public boolean addNodeAttribute(String domainServiceUrl, String machineId, String platformId, String nodeId, String name, Object value) throws
// ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.addNodeAttribute(nodeId, name, value);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @param name
// * @param value
// * @return
// * @throws ClientException
// */
// public boolean updateNodeAttribute(String domainServiceUrl, String machineId, String platformId, String nodeId, String oldName, String name, Object
// value) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.updateNodeAttribute(nodeId, oldName, name, value);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @param name
// * @return
// * @throws ClientException
// */
// public boolean deleteNodeAttribute(String domainServiceUrl, String machineId, String platformId, String nodeId, String name) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.deleteNodeAttribute(nodeId, name);
// }
// }
// return succeed;
// }

// /**
// *
// * @param nodeControlClient
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public NodeInfo getNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
// NodeInfo nodeInfo = null;
// if (nodeControlClient != null && nodeId != null) {
// nodeInfo = nodeControlClient.getNode(nodeId);
// }
// return nodeInfo;
// }

// /**
// *
// * @param nodeControlClient
// * @return
// * @throws ClientException
// */
// public NodeInfo[] getNodes(NodeControlClient nodeControlClient) throws ClientException {
// NodeInfo[] nodeInfos = null;
// if (nodeControlClient != null) {
// nodeInfos = nodeControlClient.getNodes();
// }
// if (nodeInfos == null) {
// nodeInfos = EMPTY_NODE_INFOS;
// }
// return nodeInfos;
// }
