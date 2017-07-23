package org.orbit.component.api.tier3.transferagent;

import org.orbit.component.api.Activator;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.origin.common.rest.client.ClientException;

public class TransferAgentHelper {

	private static TransferAgentHelper INSTANCE = new TransferAgentHelper();

	public static TransferAgentHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param domainMgmt
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 * @throws ClientException
	 */
	public TransferAgent getTransferAgent(DomainManagement domainMgmt, String machineId, String transferAgentId) throws ClientException {
		TransferAgent transferAgent = null;

		TransferAgentConfig taConfig = domainMgmt.getTransferAgentConfig(machineId, transferAgentId);
		if (taConfig == null) {
			System.err.println("Transfer Agent configuration is not found.");
			return null;
		}
		if (taConfig != null) {
			transferAgent = Activator.getInstance().getTransferAgentAdapter().getTransferAgent(taConfig);
		}
		if (transferAgent == null) {
			System.err.println("Transfer Agent is null.");
		}

		return transferAgent;
	}

}
