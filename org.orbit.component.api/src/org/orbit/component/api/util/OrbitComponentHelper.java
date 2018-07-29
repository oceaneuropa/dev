package org.orbit.component.api.util;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.model.tier3.domain.request.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddPlatformConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdatePlatformConfigRequest;
import org.origin.common.rest.client.ClientException;

public class OrbitComponentHelper {

	public static OrbitComponentHelper INSTANCE = new OrbitComponentHelper();

	private static final NodeInfo[] EMPTY_NODE_INFOS = new NodeInfo[0];

	/**
	 * 
	 * @param domainServiceUrl
	 * @return
	 * @throws ClientException
	 */
	public MachineConfig[] getMachineConfigs(String domainServiceUrl) throws ClientException {
		MachineConfig[] machineConfigs = null;
		if (domainServiceUrl != null) {
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
			if (domainClient != null) {
				machineConfigs = domainClient.getMachineConfigs();
			}
		}
		return machineConfigs;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public MachineConfig getMachineConfig(String domainServiceUrl, String machineId) throws ClientException {
		MachineConfig machineConfig = null;
		if (domainServiceUrl != null && machineId != null) {
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
			if (domainClient != null) {
				machineConfig = domainClient.getMachineConfig(machineId);
			}
		}
		return machineConfig;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param id
	 * @param name
	 * @param ip
	 * @return
	 * @throws ClientException
	 */
	public boolean addMachineConfig(String domainServiceUrl, String id, String name, String ip) throws ClientException {
		boolean succeed = false;
		if (domainServiceUrl != null) {
			DomainManagementClient domainMgmt = getDomainClient(domainServiceUrl);
			if (domainMgmt != null) {
				AddMachineConfigRequest addMachineRequest = new AddMachineConfigRequest();
				addMachineRequest.setMachineId(id);
				addMachineRequest.setName(name);
				addMachineRequest.setIpAddress(ip);

				succeed = domainMgmt.addMachineConfig(addMachineRequest);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param id
	 * @param name
	 * @param ip
	 * @return
	 * @throws ClientException
	 */
	public boolean updateMachineConfig(String domainServiceUrl, String id, String name, String ip) throws ClientException {
		boolean succeed = false;
		if (domainServiceUrl != null) {
			DomainManagementClient domainMgmt = getDomainClient(domainServiceUrl);
			if (domainMgmt != null) {
				UpdateMachineConfigRequest updateMachineRequest = new UpdateMachineConfigRequest();
				updateMachineRequest.setMachineId(id);
				updateMachineRequest.setName(name);
				updateMachineRequest.setIpAddress(ip);

				succeed = domainMgmt.updateMachineConfig(updateMachineRequest);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	public boolean removeMachineConfig(String domainServiceUrl, String id) throws ClientException {
		boolean succeed = false;
		if (domainServiceUrl != null) {
			DomainManagementClient domainMgmt = getDomainClient(domainServiceUrl);
			if (domainMgmt != null) {
				succeed = domainMgmt.removeMachineConfig(id);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public PlatformConfig[] getPlatformConfigs(String domainServiceUrl, String machineId) throws ClientException {
		PlatformConfig[] platformConfigs = null;
		if (domainServiceUrl != null && machineId != null) {
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
			if (domainClient != null) {
				platformConfigs = domainClient.getPlatformConfigs(machineId);
			}
		}
		return platformConfigs;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param machineId
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public PlatformConfig getPlatformConfig(String domainServiceUrl, String machineId, String platformId) throws ClientException {
		PlatformConfig platformConfig = null;
		if (domainServiceUrl != null && machineId != null) {
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
			if (domainClient != null) {
				platformConfig = domainClient.getPlatformConfig(machineId, platformId);
			}
		}
		return platformConfig;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param machineId
	 * @param platformId
	 * @param name
	 * @param hostUrl
	 * @param contextRoot
	 * @return
	 * @throws ClientException
	 */
	public boolean addPlatformConfig(String domainServiceUrl, String machineId, String platformId, String name, String hostUrl, String contextRoot) throws ClientException {
		boolean succeed = false;
		if (domainServiceUrl != null && machineId != null && platformId != null) {
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
			if (domainClient != null) {
				AddPlatformConfigRequest addPlatformRequest = new AddPlatformConfigRequest();
				addPlatformRequest.setPlatformId(platformId);
				addPlatformRequest.setName(name);
				addPlatformRequest.setHostURL(hostUrl);
				addPlatformRequest.setContextRoot(contextRoot);

				succeed = domainClient.addPlatformConfig(machineId, addPlatformRequest);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param machineId
	 * @param platformId
	 * @param name
	 * @param hostUrl
	 * @param contextRoot
	 * @return
	 * @throws ClientException
	 */
	public boolean updatePlatformConfig(String domainServiceUrl, String machineId, String platformId, String name, String hostUrl, String contextRoot) throws ClientException {
		boolean succeed = false;
		if (domainServiceUrl != null && machineId != null && platformId != null) {
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
			if (domainClient != null) {
				UpdatePlatformConfigRequest updatePlatformRequest = new UpdatePlatformConfigRequest();
				updatePlatformRequest.setPlatformId(platformId);
				updatePlatformRequest.setName(name);
				updatePlatformRequest.setHostURL(hostUrl);
				updatePlatformRequest.setContextRoot(contextRoot);

				succeed = domainClient.updatPlatformConfig(machineId, updatePlatformRequest);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param machineId
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public boolean removePlatformConfig(String domainServiceUrl, String machineId, String platformId) throws ClientException {
		boolean succeed = false;
		if (domainServiceUrl != null && machineId != null && platformId != null) {
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
			if (domainClient != null) {
				succeed = domainClient.removePlatformConfig(machineId, platformId);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param nodeControlClient
	 * @return
	 * @throws ClientException
	 */
	public NodeInfo[] getNodes(NodeControlClient nodeControlClient) throws ClientException {
		NodeInfo[] nodeInfos = null;
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
	 * @param nodeControlClient
	 * @param nodeId
	 * @return
	 * @throws ClientException
	 */
	public NodeInfo getNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
		NodeInfo nodeInfo = null;
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
	public boolean createNode(NodeControlClient nodeControlClient, String id, String name, String typeId) throws ClientException {
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
	public boolean updateNode(NodeControlClient nodeControlClient, String id, String name, String typeId) throws ClientException {
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
	public boolean deleteNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
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
	 * @return
	 * @throws ClientException
	 */
	public boolean startNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.startNode(nodeId);
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
	public boolean stopNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.stopNode(nodeId);
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
	public boolean addNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String name, Object value) throws ClientException {
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
	public boolean updateNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String oldName, String name, Object value) throws ClientException {
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
	public boolean deleteNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String name) throws ClientException {
		boolean succeed = false;
		if (nodeControlClient != null && nodeId != null) {
			succeed = nodeControlClient.deleteNodeAttribute(nodeId, name);
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @return
	 */
	public DomainManagementClient getDomainClient(String domainServiceUrl) {
		DomainManagementClient domainClient = OrbitClients.getInstance().getDomainService(domainServiceUrl);
		return domainClient;
	}

}

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
