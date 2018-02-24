package org.orbit.component.connector.tier3.nodecontrol.other;

import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeManagementClient;
import org.orbit.component.api.tier3.nodecontrol.other.TransferAgentConnector;
import org.orbit.component.connector.tier3.nodecontrol.NodeManagementClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.osgi.framework.ServiceRegistration;

public class TransferAgentConnectorImplV3 extends ServiceConnector<NodeManagementClient> implements TransferAgentConnector {

	protected ServiceRegistration<?> serviceRegistration2;

	public TransferAgentConnectorImplV3() {
		// super("transfer_agent.connector");
		super((Class<NodeManagementClient>) null);
	}

	@Override
	protected NodeManagementClient create(Map<String, Object> properties) {
		return new NodeManagementClientImpl(null, properties);
	}

}

// protected TransferAgentConfig transferAgentConfig;
// protected TransferAgent transferAgent;
//
/// **
// *
// * @param indexService
// * @param transferAgentConfig
// */
// public TransferAgentConnectorImpl(IndexService indexService, TransferAgentConfig transferAgentConfig) {
// this.transferAgentConfig = transferAgentConfig;
//
// String machineId = this.transferAgentConfig.getMachineId();
// String id = this.transferAgentConfig.getId();
// String name = this.transferAgentConfig.getName();
// String hostURL = this.transferAgentConfig.getHostURL();
// String contextRoot = this.transferAgentConfig.getContextRoot();
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(OrbitConstants.TRANSFER_AGENT_MACHINE_ID, machineId);
// properties.put(OrbitConstants.TRANSFER_AGENT_TRANSFER_AGENT_ID, id);
// properties.put(OrbitConstants.TRANSFER_AGENT_NAME, name);
// properties.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, hostURL);
// properties.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, contextRoot);
//
// this.transferAgent = new TransferAgentImpl(properties);
// }

// protected void checkProperties(Map<Object, Object> properties) throws ClientException {
// String url = (String) properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
//
// if (url == null || url.isEmpty()) {
// throw new RuntimeException("'" + OrbitConstants.TRANSFER_AGENT_HOST_URL + "' property is not set.");
// }
// if (contextRoot == null || contextRoot.isEmpty()) {
// throw new RuntimeException("'" + OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT + "' property is not set.");
// }
// }
//
// protected String getClientKey(Map<Object, Object> properties) {
// String url = (String) properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
// String token = (String) properties.get("orbit.token");
// if (token == null) {
// token = "n/a";
// }
// String key = url + KEY_PARTS_SEPARATOR + contextRoot + KEY_PARTS_SEPARATOR + token;
// return key;
// }

// @Override
// protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
// List<IndexItem> indexItems = new ArrayList<IndexItem>();
//
// String name = this.transferAgentConfig.getName();
// IndexItem indexItem = indexService.getIndexItem(OrbitConstants.TRANSFER_AGENT_INDEXER_ID, OrbitConstants.TRANSFER_AGENT_TYPE, name);
// if (indexItem != null) {
// indexItems.add(indexItem);
// }
//
// return indexItems;
// }
//
// @Override
// protected TransferAgent createService(Map<String, Object> properties) {
// return this.transferAgent;
// }
//
// @Override
// protected void updateService(TransferAgent service, Map<String, Object> properties) {
// this.transferAgent.update(properties);
// }

// @Override
// public synchronized boolean update(TransferAgent agent, Map<Object, Object> properties) throws ClientException {
// if (agent == null) {
// throw new IllegalArgumentException("TransferAgent is null.");
// }
// checkProperties(properties);
//
// if (this.clientMap.containsValue(agent)) {
// for (Iterator<String> itor = this.clientMap.keySet().iterator(); itor.hasNext();) {
// String currKey = itor.next();
// TransferAgent currAgent = this.clientMap.get(currKey);
//
// if (agent.equals(currAgent)) {
// String newKey = getClientId(properties);
//
// if (!newKey.equals(currKey)) {
// this.clientMap.remove(currKey);
// this.clientMap.put(newKey, currAgent);
// return true;
// }
// }
// }
// }
// return false;
// }

// @Override
// public synchronized boolean close(TransferAgent agent) {
// if (agent == null) {
// throw new IllegalArgumentException("TransferAgent is null.");
// }
//
// if (this.connectorMap.containsValue(agent)) {
// String keyToRemove = null;
// for (Iterator<String> itor = this.connectorMap.keySet().iterator(); itor.hasNext();) {
// String currKey = itor.next();
// TransferAgent currAgent = this.connectorMap.get(currKey);
//
// if (agent.equals(currAgent)) {
// keyToRemove = currKey;
// break;
// }
// }
// if (keyToRemove != null) {
// this.connectorMap.remove(keyToRemove);
// return true;
// }
// }
// return false;
// }
