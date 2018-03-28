package org.orbit.component.runtime.tier3.domainmanagement.service;

import java.util.List;

import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.model.tier3.domain.PlatformConfigRTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;
import org.origin.core.resources.IWorkspaceService;

public interface DomainManagementService extends WebServiceAware {

	String getName();

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	List<MachineConfigRTO> getMachineConfigs() throws ServerException;

	MachineConfigRTO getMachineConfig(String machineId) throws ServerException;

	boolean machineConfigExists(String machineId) throws ServerException;

	boolean addMachineConfig(MachineConfigRTO addMachineRequest) throws ServerException;

	boolean updateMachineConfig(MachineConfigRTO updateMachineRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deleteMachineConfig(String machineId) throws ServerException;

	// ------------------------------------------------------
	// Platform management
	// ------------------------------------------------------
	List<PlatformConfigRTO> getPlatformConfigs(String machineId) throws ServerException;

	PlatformConfigRTO getPlatformConfig(String machineId, String platformId) throws ServerException;

	boolean platformConfigExists(String machineId, String platformId) throws ServerException;

	boolean addPlatformConfig(String machineId, PlatformConfigRTO addPlatformRequest) throws ServerException;

	boolean updatePlatformConfig(String machineId, PlatformConfigRTO updatePlatformRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deletePlatformConfig(String machineId, String platformId) throws ServerException;

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	List<NodeConfigRTO> getNodeConfigs(String machineId, String platformId) throws ServerException;

	NodeConfigRTO getNodeConfig(String machineId, String platformId, String nodeId) throws ServerException;

	boolean nodeConfigExists(String machineId, String platformId, String nodeId) throws ServerException;

	boolean addNodeConfig(String machineId, String platformId, NodeConfigRTO addNodeRequest) throws ServerException;

	boolean updateNodeConfig(String machineId, String platformId, NodeConfigRTO updateNodeRequest, List<String> fieldsToUpdate) throws ServerException;

	boolean deleteNodeConfig(String machineId, String platformId, String nodeId) throws ServerException;

	// ------------------------------------------------------
	// Workspaces management
	// ------------------------------------------------------
	IWorkspaceService getWorkspaceService();

}
