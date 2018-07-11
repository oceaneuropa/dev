package org.orbit.component.runtime.util;

import java.util.Map;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.platform.api.Clients;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;
import org.origin.common.rest.client.ClientException;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

	/**
	 * 
	 * @param domainServiceUrl
	 * @param machineId
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public NodeControlClient getNodeControlClient(String domainServiceUrl, String machineId, String platformId) throws ClientException {
		NodeControlClient nodeControlClient = null;
		if (domainServiceUrl != null && machineId != null && platformId != null) {
			DomainManagementClient domainClient = OrbitClients.getInstance().getDomainService(domainServiceUrl);
			if (domainClient != null) {
				PlatformConfig platformConfig = domainClient.getPlatformConfig(machineId, platformId);
				if (platformConfig != null) {
					nodeControlClient = getNodeControlClient(platformConfig);
				}
			}
		}
		return nodeControlClient;
	}

	/**
	 * 
	 * @param platformConfig
	 * @return
	 * @throws ClientException
	 */
	private NodeControlClient getNodeControlClient(PlatformConfig platformConfig) throws ClientException {
		NodeControlClient nodeControlClient = null;
		if (platformConfig != null) {
			String url = platformConfig.getHostURL() + platformConfig.getContextRoot();
			nodeControlClient = OrbitClients.getInstance().getNodeControl(url);
		}
		return nodeControlClient;
	}

	/**
	 * 
	 * @param nodeIdToIndexItem
	 * @param nodeId
	 * @return
	 */
	public PlatformClient getNodePlatformClient(Map<String, IndexItem> nodeIdToIndexItem, String nodeId) {
		PlatformClient nodePlatformClient = null;
		if (nodeIdToIndexItem != null && nodeId != null) {
			IndexItem nodeIndexItem = nodeIdToIndexItem.get(nodeId);
			if (nodeIndexItem != null) {
				nodePlatformClient = getNodePlatformClient(nodeIndexItem);
			}
		}
		return nodePlatformClient;
	}

	/**
	 * 
	 * @param nodeIdToIndexItem
	 * @param nodeId
	 * @return
	 */
	public PlatformClient getNodePlatformClient(IndexItem nodeIndexItem) {
		PlatformClient nodePlatformClient = null;
		if (nodeIndexItem != null) {
			String nodePlatformUrl = null;
			String platformHostUrl = (String) nodeIndexItem.getProperties().get(PlatformConstants.PLATFORM_HOST_URL);
			String platformContextRoot = (String) nodeIndexItem.getProperties().get(PlatformConstants.PLATFORM_CONTEXT_ROOT);

			if (platformHostUrl != null && platformContextRoot != null) {
				nodePlatformUrl = platformHostUrl;
				if (!nodePlatformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
					nodePlatformUrl += "/";
				}
				nodePlatformUrl += platformContextRoot;
			}

			if (nodePlatformUrl != null) {
				nodePlatformClient = Clients.getInstance().getPlatformClient(nodePlatformUrl);
			}
		}
		return nodePlatformClient;
	}

}
