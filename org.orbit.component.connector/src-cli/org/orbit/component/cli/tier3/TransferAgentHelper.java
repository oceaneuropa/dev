package org.orbit.component.cli.tier3;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfig;
import org.origin.common.rest.client.ClientException;

public class TransferAgentHelper {

	private static TransferAgentHelper INSTANCE = new TransferAgentHelper();

	public static TransferAgentHelper getInstance() {
		return INSTANCE;
	}

	public TransferAgent getTransferAgent(DomainManagementConnector domainMgmtConnector, TransferAgentConnector transferAgentConnector, String machineId, String transferAgentId) throws ClientException {
		TransferAgent transferAgent = null;
		DomainManagement domainMgmt = domainMgmtConnector.getService();
		if (domainMgmt != null) {
			TransferAgentConfig transferAgentConfig = domainMgmt.getTransferAgentConfig(machineId, transferAgentId);
			if (transferAgentConfig != null) {
				transferAgent = getTransferAgent(transferAgentConnector, transferAgentConfig);
			}
		}
		return transferAgent;
	}

	public TransferAgent getTransferAgent(TransferAgentConnector transferAgentConnector, TransferAgentConfig taConfig) {
		Map<Object, Object> properties = new HashMap<Object, Object>();
		properties.put(OrbitConstants.TRANSFER_AGENT_MACHINE_ID, taConfig.getMachineId());
		properties.put(OrbitConstants.TRANSFER_AGENT_TRANSFER_AGENT_ID, taConfig.getId());
		properties.put(OrbitConstants.TRANSFER_AGENT_NAME, taConfig.getName());
		properties.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, taConfig.getHostURL());
		properties.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, taConfig.getContextRoot());
		properties.put(OrbitConstants.TRANSFER_AGENT_HOME, taConfig.getHome());

		TransferAgent transferAgent = transferAgentConnector.getService(properties);
		return transferAgent;
	}

}
