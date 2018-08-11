package org.orbit.component.runtime.tier3.domain.service;

import java.util.List;

import org.orbit.component.runtime.model.domain.MachineConfig;
import org.orbit.component.runtime.model.domain.NodeConfig;
import org.orbit.component.runtime.model.domain.PlatformConfig;
import org.origin.common.resources.IWorkspaceService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface DomainManagementService extends WebServiceAware {

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	List<MachineConfig> getMachineConfigs() throws ServerException;

	MachineConfig getMachineConfig(String machineId) throws ServerException;

	boolean machineConfigExists(String machineId) throws ServerException;

	boolean addMachineConfig(MachineConfig addMachineRequest) throws ServerException;

	boolean updateMachineConfig(MachineConfig updateMachineRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deleteMachineConfig(String machineId) throws ServerException;

	// ------------------------------------------------------
	// Platform management
	// ------------------------------------------------------
	List<PlatformConfig> getPlatformConfigs(String machineId) throws ServerException;

	PlatformConfig getPlatformConfig(String machineId, String platformId) throws ServerException;

	boolean platformConfigExists(String machineId, String platformId) throws ServerException;

	boolean addPlatformConfig(String machineId, PlatformConfig addPlatformRequest) throws ServerException;

	boolean updatePlatformConfig(String machineId, PlatformConfig updatePlatformRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deletePlatformConfig(String machineId, String platformId) throws ServerException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	List<NodeConfig> getNodeConfigs(String machineId, String platformId) throws ServerException;

	NodeConfig getNodeConfig(String machineId, String platformId, String nodeId) throws ServerException;

	boolean nodeConfigExists(String machineId, String platformId, String nodeId) throws ServerException;

	boolean addNodeConfig(String machineId, String platformId, NodeConfig addNodeRequest) throws ServerException;

	boolean updateNodeConfig(String machineId, String platformId, NodeConfig updateNodeRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deleteNodeConfig(String machineId, String platformId, String nodeId) throws ServerException;

	// ------------------------------------------------------
	// Workspaces management
	// ------------------------------------------------------
	IWorkspaceService getWorkspaceService();

}
