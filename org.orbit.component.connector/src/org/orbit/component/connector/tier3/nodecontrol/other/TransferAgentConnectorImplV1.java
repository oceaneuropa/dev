package org.orbit.component.connector.tier3.nodecontrol.other;

import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeManagementClient;
import org.orbit.component.api.tier3.nodecontrol.other.TransferAgentConnector;
import org.origin.common.rest.client.ServiceConnector;

public class TransferAgentConnectorImplV1 extends ServiceConnector<NodeManagementClient> implements TransferAgentConnector {

	public TransferAgentConnectorImplV1() {
		super((Class<NodeManagementClient>) null);
	}

	@Override
	protected NodeManagementClient create(Map<String, Object> properties) {
		return null;
	}

}

// protected TransferAgentConfig transferAgentConfig;
// protected TransferAgent transferAgent;
//
// /**
// *
// * @param indexService
// * @param transferAgentConfig
// */
// public TransferAgentConnectorImpl(IndexService indexService, TransferAgentConfig transferAgentConfig) {
// super(indexService, TransferAgentConnector.class);
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
// this.transferAgent = new TransferAgentWSImpl(properties);
// }
//
// @Override
// public TransferAgent getService() {
// return this.transferAgent;
// }
//
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