package org.orbit.component.api.tier3.domain;

import org.origin.common.rest.model.Responses;

public interface DomainManagementResponseConverter {

	/**
	 * 
	 * @param responses
	 * @return
	 */
	MachineConfig[] convertToMachineConfigs(Responses responses);

	/**
	 * 
	 * @param responses
	 * @return
	 */
	MachineConfig convertToMachineConfig(Responses responses);

	/**
	 * 
	 * @param responses
	 * @return
	 */
	TransferAgentConfig[] convertToTransferAgentConfigs(Responses responses);

	/**
	 * 
	 * @param responses
	 * @return
	 */
	TransferAgentConfig convertToTransferAgentConfig(Responses responses);

	/**
	 * 
	 * @param responses
	 * @return
	 */
	NodeConfig[] convertToNodeConfigs(Responses responses);

	/**
	 * 
	 * @param responses
	 * @return
	 */
	NodeConfig convertToNodeConfig(Responses responses);

}
