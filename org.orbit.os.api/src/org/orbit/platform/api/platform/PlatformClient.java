package org.orbit.platform.api.platform;

import java.util.List;
import java.util.Map;

import org.orbit.platform.model.platform.dto.ServiceExtensionInfo;
import org.orbit.platform.model.platform.dto.ServiceInstanceInfo;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface PlatformClient extends ServiceClient {

	/**
	 * 
	 * @return
	 * @throws ClientException
	 */
	List<String> getExtensionTypeIds() throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @return
	 * @throws ClientException
	 */
	List<ServiceExtensionInfo> getServiceExtensions(String extensionTypeId) throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @return
	 * @throws ClientException
	 */
	ServiceExtensionInfo getServiceExtension(String extensionTypeId, String extensionId) throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @return
	 * @throws ClientException
	 */
	List<ServiceInstanceInfo> getServiceInstances(String extensionTypeId) throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @return
	 * @throws ClientException
	 */
	ServiceInstanceInfo getServiceInstance(String extensionTypeId, String extensionId) throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	boolean startService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	boolean stopService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException;

}
