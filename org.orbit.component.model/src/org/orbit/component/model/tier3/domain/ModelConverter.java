package org.orbit.component.model.tier3.domain;

import org.origin.common.rest.model.ErrorDTO;

public class ModelConverter {

	private static ModelConverter converter = new ModelConverter();

	public static ModelConverter getInstance() {
		return converter;
	}

	// ------------------------------------------------------------------------------------------
	// RTO to DTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert DomainManagementException object to Error DTO.
	 * 
	 * @param e
	 * @return
	 */
	public ErrorDTO toDTO(DomainMgmtException e) {
		if (e == null) {
			return null;
		}

		ErrorDTO dto = new ErrorDTO();

		dto.setCode(e.getCode());
		dto.setMessage(e.getMessage());

		if (e.getCause() != null) {
			String causeName = e.getCause().getClass().getName();
			String causeMessage = e.getCause().getMessage();
			dto.setException(causeName + " " + causeMessage);
		} else {
			String causeName = e.getClass().getName();
			dto.setException(causeName);
		}

		return dto;
	}

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
	public TransferAgentConfigDTO toDTO(TransferAgentConfigRTO transferAgentConfig) {
		if (transferAgentConfig == null) {
			return null;
		}
		TransferAgentConfigDTO dto = new TransferAgentConfigDTO();

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
		dto.setTransferAgentId(nodeConfig.getTransferAgentId());
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
	public TransferAgentConfigRTO toRTO(TransferAgentConfigDTO transferAgentConfigDTO) {
		if (transferAgentConfigDTO == null) {
			return null;
		}
		TransferAgentConfigRTO transferAgentConfig = new TransferAgentConfigRTO();

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
		nodeConfig.setTransferAgentId(nodeConfigDTO.getTransferAgentId());
		nodeConfig.setName(nodeConfigDTO.getName());
		nodeConfig.setHome(nodeConfigDTO.getHome());
		nodeConfig.setHostURL(nodeConfigDTO.getHostURL());
		nodeConfig.setContextRoot(nodeConfigDTO.getContextRoot());

		return nodeConfig;
	}

}
