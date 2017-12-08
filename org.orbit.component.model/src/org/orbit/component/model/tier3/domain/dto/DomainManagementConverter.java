package org.orbit.component.model.tier3.domain.dto;

import java.util.Map;

import org.orbit.component.model.tier3.domain.request.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddNodeConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddTransferAgentConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateNodeConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateTransferAgentConfigRequest;

public class DomainManagementConverter {

	public static DomainManagementConverter INSTANCE = new DomainManagementConverter();

	/**
	 * 
	 * @param machineDTO
	 * @return
	 */
	public MachineConfig toMachineConfig(MachineConfigDTO machineDTO) {
		MachineConfigImpl impl = new MachineConfigImpl();
		impl.setId(machineDTO.getId());
		impl.setName(machineDTO.getName());
		impl.setIpAddress(machineDTO.getIpAddress());
		return impl;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public MachineConfig toMachineConfig(Map<?, ?> map) {
		String machineId = (String) map.get("id");
		String machineName = (String) map.get("name");
		String ipAddress = (String) map.get("ipAddress");

		MachineConfigImpl impl = new MachineConfigImpl();
		impl.setId(machineId);
		impl.setName(machineName);
		impl.setIpAddress(ipAddress);
		return impl;
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentDTO
	 * @return
	 */
	public TransferAgentConfig toTransferAgentConfig(String machineId, TransferAgentConfigDTO transferAgentDTO) {
		TransferAgentConfigImpl impl = new TransferAgentConfigImpl();
		impl.setMachineId(machineId);
		impl.setId(transferAgentDTO.getId());
		impl.setName(transferAgentDTO.getName());
		impl.setHome(transferAgentDTO.getHome());
		impl.setHostURL(transferAgentDTO.getHostURL());
		impl.setContextRoot(transferAgentDTO.getContextRoot());
		return impl;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public TransferAgentConfig toTransferAgentConfig(Map<?, ?> map) {
		String machineId = (String) map.get("machineId");
		String id = (String) map.get("id");
		String name = (String) map.get("name");
		String home = (String) map.get("home");
		String hostURL = (String) map.get("hostURL");
		String contextRoot = (String) map.get("contextRoot");

		TransferAgentConfigImpl impl = new TransferAgentConfigImpl();
		impl.setMachineId(machineId);
		impl.setId(id);
		impl.setName(name);
		impl.setHome(home);
		impl.setHostURL(hostURL);
		impl.setContextRoot(contextRoot);

		return impl;
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeConfigDTO
	 * @return
	 */
	public NodeConfig toNodeConfig(String machineId, String transferAgentId, NodeConfigDTO nodeConfigDTO) {
		NodeConfigImpl impl = new NodeConfigImpl();
		impl.setMachineId(machineId);
		impl.setTransferAgentId(transferAgentId);
		impl.setId(nodeConfigDTO.getId());
		impl.setName(nodeConfigDTO.getName());
		impl.setHome(nodeConfigDTO.getHome());
		impl.setHostURL(nodeConfigDTO.getHostURL());
		impl.setContextRoot(nodeConfigDTO.getContextRoot());
		return impl;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public NodeConfig toNodeConfig(Map<?, ?> map) {
		String machineId = (String) map.get("machineId");
		String transferAgentId = (String) map.get("transferAgentId");
		String id = (String) map.get("id");
		String name = (String) map.get("name");
		String home = (String) map.get("home");
		String hostURL = (String) map.get("hostURL");
		String contextRoot = (String) map.get("contextRoot");

		NodeConfigImpl impl = new NodeConfigImpl();
		impl.setMachineId(machineId);
		impl.setTransferAgentId(transferAgentId);
		impl.setId(id);
		impl.setName(name);
		impl.setHome(home);
		impl.setHostURL(hostURL);
		impl.setContextRoot(contextRoot);

		return impl;
	}

	/**
	 * 
	 * @param addMachineRequest
	 * @return
	 */
	public MachineConfigDTO toDTO(AddMachineConfigRequest addMachineRequest) {
		MachineConfigDTO addMachineRequestDTO = new MachineConfigDTO();
		addMachineRequestDTO.setId(addMachineRequest.getMachineId());
		addMachineRequestDTO.setName(addMachineRequest.getName());
		addMachineRequestDTO.setIpAddress(addMachineRequest.getIpAddress());
		return addMachineRequestDTO;
	}

	/**
	 * 
	 * @param updateMachineRequest
	 * @return
	 */
	public MachineConfigDTO toDTO(UpdateMachineConfigRequest updateMachineRequest) {
		MachineConfigDTO updateMachineRequestDTO = new MachineConfigDTO();
		updateMachineRequestDTO.setId(updateMachineRequest.getMachineId());
		updateMachineRequestDTO.setName(updateMachineRequest.getName());
		updateMachineRequestDTO.setIpAddress(updateMachineRequest.getIpAddress());
		updateMachineRequestDTO.setFieldsToUpdate(updateMachineRequest.getFieldsToUpdate());
		return updateMachineRequestDTO;
	}

	/**
	 * 
	 * @param addTransferAgentRequest
	 * @return
	 */
	public TransferAgentConfigDTO toDTO(AddTransferAgentConfigRequest addTransferAgentRequest) {
		TransferAgentConfigDTO addTransferAgentRequestDTO = new TransferAgentConfigDTO();
		addTransferAgentRequestDTO.setId(addTransferAgentRequest.getTransferAgentId());
		addTransferAgentRequestDTO.setName(addTransferAgentRequest.getName());
		addTransferAgentRequestDTO.setHome(addTransferAgentRequest.getHome());
		addTransferAgentRequestDTO.setHostURL(addTransferAgentRequest.getHostURL());
		addTransferAgentRequestDTO.setContextRoot(addTransferAgentRequest.getContextRoot());
		return addTransferAgentRequestDTO;
	}

	/**
	 * 
	 * @param updateTransferAgentRequest
	 * @return
	 */
	public TransferAgentConfigDTO toDTO(UpdateTransferAgentConfigRequest updateTransferAgentRequest) {
		TransferAgentConfigDTO updateTransferAgentRequestDTO = new TransferAgentConfigDTO();
		updateTransferAgentRequestDTO.setId(updateTransferAgentRequest.getTransferAgentId());
		updateTransferAgentRequestDTO.setName(updateTransferAgentRequest.getName());
		updateTransferAgentRequestDTO.setHome(updateTransferAgentRequest.getHome());
		updateTransferAgentRequestDTO.setHostURL(updateTransferAgentRequest.getHostURL());
		updateTransferAgentRequestDTO.setContextRoot(updateTransferAgentRequest.getContextRoot());
		return updateTransferAgentRequestDTO;
	}

	/**
	 * 
	 * @param addNodeRequest
	 * @return
	 */
	public NodeConfigDTO toDTO(AddNodeConfigRequest addNodeRequest) {
		NodeConfigDTO addNodeRequestDTO = new NodeConfigDTO();
		addNodeRequestDTO.setId(addNodeRequest.getNodeId());
		addNodeRequestDTO.setName(addNodeRequest.getName());
		addNodeRequestDTO.setHome(addNodeRequest.getHome());
		addNodeRequestDTO.setHostURL(addNodeRequest.getHostURL());
		addNodeRequestDTO.setContextRoot(addNodeRequest.getContextRoot());
		return addNodeRequestDTO;
	}

	/**
	 * 
	 * @param updateNodeRequest
	 * @return
	 */
	public NodeConfigDTO toDTO(UpdateNodeConfigRequest updateNodeRequest) {
		NodeConfigDTO updateNodeRequestDTO = new NodeConfigDTO();
		updateNodeRequestDTO.setId(updateNodeRequest.getNodeId());
		updateNodeRequestDTO.setName(updateNodeRequest.getName());
		updateNodeRequestDTO.setHome(updateNodeRequest.getHome());
		updateNodeRequestDTO.setHostURL(updateNodeRequest.getHostURL());
		updateNodeRequestDTO.setContextRoot(updateNodeRequest.getContextRoot());
		return updateNodeRequestDTO;
	}

}
