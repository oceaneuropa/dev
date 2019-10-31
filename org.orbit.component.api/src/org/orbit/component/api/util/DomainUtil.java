package org.orbit.component.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.model.tier3.domain.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.AddPlatformConfigRequest;
import org.orbit.component.model.tier3.domain.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.UpdatePlatformConfigRequest;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;

public class DomainUtil {

	protected static MachineConfig[] EMPTY_MACHINE_CONFIGS = new MachineConfig[0];
	protected static PlatformConfig[] EMPTY_PLATFORM_CONFIGS = new PlatformConfig[0];

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @return
	 */
	public static DomainManagementClient getClient(String domainServiceUrl, String accessToken) {
		DomainManagementClient domainClient = null;
		if (domainServiceUrl != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(WSClientConstants.REALM, null);
			properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
			properties.put(WSClientConstants.URL, domainServiceUrl);
			domainClient = ComponentClients.getInstance().getDomainClient(properties);
		}
		return domainClient;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @return
	 * @throws ClientException
	 */
	public static MachineConfig[] getMachineConfigs(String domainServiceUrl, String accessToken) throws ClientException {
		MachineConfig[] machineConfigs = null;
		DomainManagementClient domainClient = getClient(domainServiceUrl, accessToken);
		if (domainClient != null) {
			machineConfigs = domainClient.getMachineConfigs();
		}
		if (machineConfigs == null) {
			machineConfigs = EMPTY_MACHINE_CONFIGS;
		}
		return machineConfigs;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public static MachineConfig getMachineConfig(String domainServiceUrl, String accessToken, String machineId) throws ClientException {
		MachineConfig machineConfig = null;
		DomainManagementClient domainClient = getClient(domainServiceUrl, accessToken);
		if (domainClient != null) {
			machineConfig = domainClient.getMachineConfig(machineId);
		}
		return machineConfig;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param id
	 * @param name
	 * @param ip
	 * @return
	 * @throws ClientException
	 */
	public static boolean addMachineConfig(String domainServiceUrl, String accessToken, String id, String name, String ip) throws ClientException {
		boolean succeed = false;
		DomainManagementClient domainMgmt = getClient(domainServiceUrl, accessToken);
		if (domainMgmt != null) {
			AddMachineConfigRequest addMachineRequest = new AddMachineConfigRequest();
			addMachineRequest.setMachineId(id);
			addMachineRequest.setName(name);
			addMachineRequest.setIpAddress(ip);
			succeed = domainMgmt.addMachineConfig(addMachineRequest);
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param id
	 * @param name
	 * @param ip
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateMachineConfig(String domainServiceUrl, String accessToken, String id, String name, String ip) throws ClientException {
		boolean succeed = false;
		DomainManagementClient domainMgmt = getClient(domainServiceUrl, accessToken);
		if (domainMgmt != null) {
			UpdateMachineConfigRequest updateMachineRequest = new UpdateMachineConfigRequest();
			updateMachineRequest.setMachineId(id);
			updateMachineRequest.setName(name);
			updateMachineRequest.setIpAddress(ip);
			succeed = domainMgmt.updateMachineConfig(updateMachineRequest);
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	public static boolean removeMachineConfig(String domainServiceUrl, String accessToken, String id) throws ClientException {
		boolean succeed = false;
		DomainManagementClient domainMgmt = getClient(domainServiceUrl, accessToken);
		if (domainMgmt != null) {
			succeed = domainMgmt.removeMachineConfig(id);
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public static PlatformConfig[] getPlatformConfigs(String domainServiceUrl, String accessToken, String machineId) throws ClientException {
		PlatformConfig[] platformConfigs = null;
		DomainManagementClient domainClient = getClient(domainServiceUrl, accessToken);
		if (domainClient != null) {
			platformConfigs = domainClient.getPlatformConfigs(machineId);
		}
		if (platformConfigs == null) {
			platformConfigs = EMPTY_PLATFORM_CONFIGS;
		}
		return platformConfigs;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param machineId
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public static PlatformConfig getPlatformConfig(String domainServiceUrl, String accessToken, String machineId, String platformId) throws ClientException {
		PlatformConfig platformConfig = null;
		DomainManagementClient domainClient = getClient(domainServiceUrl, accessToken);
		if (domainClient != null) {
			platformConfig = domainClient.getPlatformConfig(machineId, platformId);
		}
		return platformConfig;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param machineId
	 * @param platformId
	 * @param name
	 * @param hostUrl
	 * @param contextRoot
	 * @return
	 * @throws ClientException
	 */
	public static boolean addPlatformConfig(String domainServiceUrl, String accessToken, String machineId, String platformId, String name, String hostUrl, String contextRoot) throws ClientException {
		boolean succeed = false;
		DomainManagementClient domainClient = getClient(domainServiceUrl, accessToken);
		if (domainClient != null) {
			AddPlatformConfigRequest addPlatformRequest = new AddPlatformConfigRequest();
			addPlatformRequest.setPlatformId(platformId);
			addPlatformRequest.setName(name);
			addPlatformRequest.setHostURL(hostUrl);
			addPlatformRequest.setContextRoot(contextRoot);
			succeed = domainClient.addPlatformConfig(machineId, addPlatformRequest);
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param machineId
	 * @param platformId
	 * @param name
	 * @param hostUrl
	 * @param contextRoot
	 * @return
	 * @throws ClientException
	 */
	public static boolean updatePlatformConfig(String domainServiceUrl, String accessToken, String machineId, String platformId, String name, String hostUrl, String contextRoot) throws ClientException {
		boolean succeed = false;
		DomainManagementClient domainClient = getClient(domainServiceUrl, accessToken);
		if (domainClient != null) {
			UpdatePlatformConfigRequest updatePlatformRequest = new UpdatePlatformConfigRequest();
			updatePlatformRequest.setPlatformId(platformId);
			updatePlatformRequest.setName(name);
			updatePlatformRequest.setHostURL(hostUrl);
			updatePlatformRequest.setContextRoot(contextRoot);
			succeed = domainClient.updatPlatformConfig(machineId, updatePlatformRequest);
		}
		return succeed;
	}

	/**
	 * 
	 * @param domainServiceUrl
	 * @param accessToken
	 * @param machineId
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public static boolean removePlatformConfig(String domainServiceUrl, String accessToken, String machineId, String platformId) throws ClientException {
		boolean succeed = false;
		DomainManagementClient domainClient = getClient(domainServiceUrl, accessToken);
		if (domainClient != null) {
			succeed = domainClient.removePlatformConfig(machineId, platformId);
		}
		return succeed;
	}

}
