package org.orbit.component.connector.tier3.transferagent.other;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;
import org.origin.common.rest.client.ClientException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class TransferAgentConnectorImplV2 implements TransferAgentConnector {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, TransferAgent> transferAgentMap;

	public TransferAgentConnectorImplV2() {
		this.transferAgentMap = new HashMap<String, TransferAgent>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(TransferAgentConnector.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		this.transferAgentMap.clear();
	}

	// @Override
	// public TransferAgent getService(Map<Object, Object> properties) {
	// TransferAgent transferAGent = null;
	// String url = (String) properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
	// String contextRoot = (String) properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
	// if (url != null && contextRoot != null) {
	// String key = url + KEY_PARTS_SEPARATOR + contextRoot;
	// transferAGent = this.transferAgentMap.get(key);
	// if (transferAGent == null) {
	// transferAGent = new TransferAgentImpl(properties);
	// this.transferAgentMap.put(key, transferAGent);
	// }
	// }
	// return transferAGent;
	// }

	@Override
	public boolean update(TransferAgent agent, Map<String, Object> properties) throws ClientException {
		return false;
	}

	@Override
	public boolean close(TransferAgent agent) throws ClientException {
		return false;
	}

	@Override
	public TransferAgent getService(Map<String, Object> properties) throws ClientException {
		return null;
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
