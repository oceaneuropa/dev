package org.orbit.component.connector.tier3.transferagent;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfig;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnectorAdapter;

public class TransferAgentHelper {

	private static TransferAgentHelper INSTANCE = new TransferAgentHelper();

	public static TransferAgentHelper getInstance() {
		return INSTANCE;
	}

	public TransferAgent getTransferAgent(DomainManagementConnector domainConnector, TransferAgentConnector taConnector, String machineId, String transferAgentId) throws ClientException {
		DomainManagement domain = domainConnector.getService();
		if (domain != null) {
			TransferAgentConfig taConfig = domain.getTransferAgentConfig(machineId, transferAgentId);
			if (taConfig != null) {
				Map<Object, Object> properties = new HashMap<Object, Object>();
				properties.put(OrbitConstants.TRANSFER_AGENT_MACHINE_ID, taConfig.getMachineId());
				properties.put(OrbitConstants.TRANSFER_AGENT_TRANSFER_AGENT_ID, taConfig.getId());
				properties.put(OrbitConstants.TRANSFER_AGENT_NAME, taConfig.getName());
				properties.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, taConfig.getHostURL());
				properties.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, taConfig.getContextRoot());
				properties.put(OrbitConstants.TRANSFER_AGENT_HOME, taConfig.getHome());

				TransferAgent transferAgent = taConnector.getService(properties);
				return transferAgent;
			}
		}
		return null;
	}

	public TransferAgent getTransferAgent(String token, DomainManagementConnector domainConnector, ServiceConnectorAdapter<TransferAgent> taConnector, String machineId, String transferAgentId) throws ClientException {
		DomainManagement domain = domainConnector.getService();
		if (domain != null) {
			TransferAgentConfig taConfig = domain.getTransferAgentConfig(machineId, transferAgentId);
			if (taConfig != null) {
				String url = taConfig.getHostURL() + taConfig.getContextRoot();
				Map<Object, Object> properties = new HashMap<Object, Object>();
				properties.put(OrbitConstants.ORBIT_URL, url);
				properties.put(OrbitConstants.ORBIT_TOKEN, token);

				TransferAgent transferAgent = taConnector.getService(properties);
				return transferAgent;
			}
		}
		return null;
	}

}

// public TransferAgent getTransferAgent(DomainManagementConnector domainMgmtConnector, ServiceConnectorAdapterV1<TransferAgent> transferAgentConnector, String
// machineId, String transferAgentId) throws ClientException {
// TransferAgent transferAgent = null;
// DomainManagement domainMgmt = domainMgmtConnector.getService();
// if (domainMgmt != null) {
// TransferAgentConfig taConfig = domainMgmt.getTransferAgentConfig(machineId, transferAgentId);
// if (taConfig != null) {
// String url = taConfig.getHostURL() + taConfig.getContextRoot();
// Map<Object, Object> properties = new HashMap<Object, Object>();
// properties.put(OrbitConstants.ORBIT_URL, url);
// transferAgent = transferAgentConnector.getService(properties);
// }
// }
// return transferAgent;
// }
//
// public TransferAgent getTransferAgent(DomainManagementConnector domainMgmtConnector, ServiceConnectorAdapterV2 transferAgentConnector, String machineId,
// String transferAgentId) throws ClientException {
// TransferAgent transferAgent = null;
// DomainManagement domainMgmt = domainMgmtConnector.getService();
// if (domainMgmt != null) {
// TransferAgentConfig taConfig = domainMgmt.getTransferAgentConfig(machineId, transferAgentId);
// if (taConfig != null) {
// String url = taConfig.getHostURL() + taConfig.getContextRoot();
// Map<Object, Object> properties = new HashMap<Object, Object>();
// properties.put(OrbitConstants.ORBIT_URL, url);
// transferAgent = transferAgentConnector.getService(TransferAgent.class, properties);
// }
// }
// return transferAgent;
// }