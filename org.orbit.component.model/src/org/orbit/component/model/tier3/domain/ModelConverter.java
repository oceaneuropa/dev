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
		dto.setTAHome(transferAgentConfig.getTAHome());
		dto.setHostURL(transferAgentConfig.getHostURL());
		dto.setContextRoot(transferAgentConfig.getContextRoot());

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
	 * Convert TransferAgentConfigRTO to TransferAgentConfigDTO.
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
		transferAgentConfig.setTAHome(transferAgentConfigDTO.getTAHome());
		transferAgentConfig.setHostURL(transferAgentConfigDTO.getHostURL());
		transferAgentConfig.setContextRoot(transferAgentConfigDTO.getContextRoot());

		return transferAgentConfig;
	}

}
