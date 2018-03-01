package org.orbit.platform.api;

import java.util.List;
import java.util.Map;

import org.orbit.platform.model.dto.ExtensionInfo;
import org.orbit.platform.model.dto.ProcessInfo;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface PlatformClient extends ServiceClient {

	/**
	 * 
	 * @return
	 * @throws ClientException
	 */
	List<ExtensionInfo> getExtensions() throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @return
	 * @throws ClientException
	 */
	List<ExtensionInfo> getExtensions(String extensionTypeId) throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @return
	 * @throws ClientException
	 */
	ExtensionInfo getExtension(String extensionTypeId, String extensionId) throws ClientException;

	/**
	 * 
	 * @return
	 * @throws ClientException
	 */
	List<ProcessInfo> getProcesses() throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @return
	 * @throws ClientException
	 */
	List<ProcessInfo> getProcesses(String extensionTypeId) throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @return
	 * @throws ClientException
	 */
	List<ProcessInfo> getProcesses(String extensionTypeId, String extensionId) throws ClientException;

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	ProcessInfo startService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException;

	/**
	 * 
	 * @param pid
	 * @return
	 * @throws ClientException
	 */
	boolean stopService(int pid) throws ClientException;

}
