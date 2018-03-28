package org.orbit.component.model.tier3.domain.dto;

import java.util.Map;

import org.orbit.component.model.tier3.domain.request.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddNodeConfigRequest;
import org.orbit.component.model.tier3.domain.request.AddPlatformConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateNodeConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdatePlatformConfigRequest;

public class ModelConverter {

	public static ModelConverter INSTANCE = new ModelConverter();

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
	 * @param platformConfigDTO
	 * @return
	 */
	public PlatformConfig toPlatformConfig(String machineId, PlatformConfigDTO platformConfigDTO) {
		PlatformConfigImpl impl = new PlatformConfigImpl();
		impl.setMachineId(machineId);
		impl.setId(platformConfigDTO.getId());
		impl.setName(platformConfigDTO.getName());
		impl.setHome(platformConfigDTO.getHome());
		impl.setHostURL(platformConfigDTO.getHostURL());
		impl.setContextRoot(platformConfigDTO.getContextRoot());
		return impl;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public PlatformConfig toPlatformConfig(Map<?, ?> map) {
		String machineId = (String) map.get("machineId");
		String id = (String) map.get("id");
		String name = (String) map.get("name");
		String home = (String) map.get("home");
		String hostURL = (String) map.get("hostURL");
		String contextRoot = (String) map.get("contextRoot");

		PlatformConfigImpl impl = new PlatformConfigImpl();
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
	 * @param platformId
	 * @param nodeConfigDTO
	 * @return
	 */
	public NodeConfig toNodeConfig(String machineId, String platformId, NodeConfigDTO nodeConfigDTO) {
		NodeConfigImpl impl = new NodeConfigImpl();
		impl.setMachineId(machineId);
		impl.setPlatformId(platformId);
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
		String platformId = (String) map.get("platformId");
		String id = (String) map.get("id");
		String name = (String) map.get("name");
		String home = (String) map.get("home");
		String hostURL = (String) map.get("hostURL");
		String contextRoot = (String) map.get("contextRoot");

		NodeConfigImpl impl = new NodeConfigImpl();
		impl.setMachineId(machineId);
		impl.setPlatformId(platformId);
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
	 * @param addPlatformConfig
	 * @return
	 */
	public PlatformConfigDTO toDTO(AddPlatformConfigRequest addPlatformConfig) {
		PlatformConfigDTO addPlatformConfigDTO = new PlatformConfigDTO();
		addPlatformConfigDTO.setId(addPlatformConfig.getPlatformId());
		addPlatformConfigDTO.setName(addPlatformConfig.getName());
		addPlatformConfigDTO.setHome(addPlatformConfig.getHome());
		addPlatformConfigDTO.setHostURL(addPlatformConfig.getHostURL());
		addPlatformConfigDTO.setContextRoot(addPlatformConfig.getContextRoot());
		return addPlatformConfigDTO;
	}

	/**
	 * 
	 * @param updatePlatformConfig
	 * @return
	 */
	public PlatformConfigDTO toDTO(UpdatePlatformConfigRequest updatePlatformConfig) {
		PlatformConfigDTO updatePlatformConfigDTO = new PlatformConfigDTO();
		updatePlatformConfigDTO.setId(updatePlatformConfig.getPlatformId());
		updatePlatformConfigDTO.setName(updatePlatformConfig.getName());
		updatePlatformConfigDTO.setHome(updatePlatformConfig.getHome());
		updatePlatformConfigDTO.setHostURL(updatePlatformConfig.getHostURL());
		updatePlatformConfigDTO.setContextRoot(updatePlatformConfig.getContextRoot());
		return updatePlatformConfigDTO;
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
