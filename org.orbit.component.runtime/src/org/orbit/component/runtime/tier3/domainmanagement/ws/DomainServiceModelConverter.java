package org.orbit.component.runtime.tier3.domainmanagement.ws;

import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.PlatformConfigRTO;
import org.orbit.component.model.tier3.domain.PlatformConfigDTO;

public class DomainServiceModelConverter {

	private static DomainServiceModelConverter converter = new DomainServiceModelConverter();

	public static DomainServiceModelConverter getInstance() {
		return converter;
	}

	// ------------------------------------------------------------------------------------------
	// RTO to DTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert MachineConfigRTO to MachineConfigDTO.
	 * 
	 * @param machineConfig
	 * @return
	 */
	public MachineConfigDTO toDTO(MachineConfigRTO machineConfig) {
		if (machineConfig == null) {
			return null;
		}
		MachineConfigDTO dto = new MachineConfigDTO();

		dto.setId(machineConfig.getId());
		dto.setName(machineConfig.getName());
		dto.setIpAddress(machineConfig.getIpAddress());

		return dto;
	}

	/**
	 * Convert TransferAgentConfigRTO to TransferAgentConfigDTO.
	 * 
	 * @param transferAgentConfig
	 * @return
	 */
	public PlatformConfigDTO toDTO(PlatformConfigRTO transferAgentConfig) {
		if (transferAgentConfig == null) {
			return null;
		}
		PlatformConfigDTO dto = new PlatformConfigDTO();

		dto.setId(transferAgentConfig.getId());
		dto.setName(transferAgentConfig.getName());
		dto.setHome(transferAgentConfig.getHome());
		dto.setHostURL(transferAgentConfig.getHostURL());
		dto.setContextRoot(transferAgentConfig.getContextRoot());

		return dto;
	}

	/**
	 * Convert NodeConfigRTO to NodeConfigDTO.
	 * 
	 * @param nodeConfig
	 * @return
	 */
	public NodeConfigDTO toDTO(NodeConfigRTO nodeConfig) {
		if (nodeConfig == null) {
			return null;
		}
		NodeConfigDTO dto = new NodeConfigDTO();

		dto.setId(nodeConfig.getId());
		dto.setMachineId(nodeConfig.getMachineId());
		dto.setPlatformId(nodeConfig.getPlatformId());
		dto.setName(nodeConfig.getName());
		dto.setHome(nodeConfig.getHome());
		dto.setHostURL(nodeConfig.getHostURL());
		dto.setContextRoot(nodeConfig.getContextRoot());

		return dto;
	}

	// ------------------------------------------------------------------------------------------
	// DTO to RTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert MachineConfigDTO to MachineConfigRTO.
	 * 
	 * @param machineConfigDTO
	 * @return
	 */
	public MachineConfigRTO toRTO(MachineConfigDTO machineConfigDTO) {
		if (machineConfigDTO == null) {
			return null;
		}
		MachineConfigRTO machineConfig = new MachineConfigRTO();

		machineConfig.setId(machineConfigDTO.getId());
		machineConfig.setName(machineConfigDTO.getName());
		machineConfig.setIpAddress(machineConfigDTO.getIpAddress());

		return machineConfig;
	}

	/**
	 * Convert TransferAgentConfigDTO to TransferAgentConfigRTO.
	 * 
	 * @param transferAgentConfigDTO
	 * @return
	 */
	public PlatformConfigRTO toRTO(PlatformConfigDTO transferAgentConfigDTO) {
		if (transferAgentConfigDTO == null) {
			return null;
		}
		PlatformConfigRTO transferAgentConfig = new PlatformConfigRTO();

		transferAgentConfig.setId(transferAgentConfigDTO.getId());
		transferAgentConfig.setName(transferAgentConfigDTO.getName());
		transferAgentConfig.setHome(transferAgentConfigDTO.getHome());
		transferAgentConfig.setHostURL(transferAgentConfigDTO.getHostURL());
		transferAgentConfig.setContextRoot(transferAgentConfigDTO.getContextRoot());

		return transferAgentConfig;
	}

	/**
	 * Convert NodeConfigDTO to NodeConfigRTO.
	 * 
	 * @param nodeConfigDTO
	 * @return
	 */
	public NodeConfigRTO toRTO(NodeConfigDTO nodeConfigDTO) {
		if (nodeConfigDTO == null) {
			return null;
		}
		NodeConfigRTO nodeConfig = new NodeConfigRTO();

		nodeConfig.setId(nodeConfigDTO.getId());
		nodeConfig.setMachineId(nodeConfigDTO.getMachineId());
		nodeConfig.setPlatformId(nodeConfigDTO.getPlatformId());
		nodeConfig.setName(nodeConfigDTO.getName());
		nodeConfig.setHome(nodeConfigDTO.getHome());
		nodeConfig.setHostURL(nodeConfigDTO.getHostURL());
		nodeConfig.setContextRoot(nodeConfigDTO.getContextRoot());

		return nodeConfig;
	}

}
