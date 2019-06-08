package org.orbit.infra.runtime.extensionregistry.service;

import java.util.List;
import java.util.Map;

import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenAware;
import org.origin.common.service.WebServiceAware;

public interface ExtensionRegistryService extends WebServiceAware, AccessTokenAware {

	/**
	 * 
	 * @param platformId
	 * @return
	 * @throws ServerException
	 */
	public List<ExtensionItem> getExtensionItems(String platformId) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @return
	 * @throws ServerException
	 */
	public List<ExtensionItem> getExtensionItems(String platformId, String typeId) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws ServerException
	 */
	public ExtensionItem getExtensionItem(String platformId, String typeId, String extensionId) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param name
	 * @param description
	 * @param properties
	 * @return
	 * @throws ServerException
	 */
	public ExtensionItem addExtensionItem(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param newTypeId
	 * @param newExtensionId
	 * @param newName
	 * @param newDescription
	 * @param properties
	 * @return
	 * @throws ServerException
	 */
	public boolean updateExtensionItem(String platformId, String typeId, String extensionId, String newTypeId, String newExtensionId, String newName, String newDescription, Map<String, Object> properties) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param disposeWhenEmpty
	 * @return
	 * @throws ServerException
	 */
	public boolean removeExtensionItems(String platformId, boolean disposeWhenEmpty) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws ServerException
	 */
	public boolean removeExtensionItem(String platformId, String typeId, String extensionId) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws ServerException
	 */
	public Map<String, Object> getProperties(String platformId, String typeId, String extensionId) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param properties
	 * @return
	 * @throws ServerException
	 */
	public boolean setProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws ServerException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param propNames
	 * @return
	 * @throws ServerException
	 */
	public boolean removeProperties(String platformId, String typeId, String extensionId, List<String> propNames) throws ServerException;

}
