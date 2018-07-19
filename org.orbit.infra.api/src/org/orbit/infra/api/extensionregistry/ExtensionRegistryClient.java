package org.orbit.infra.api.extensionregistry;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.service.ProxyService;

public interface ExtensionRegistryClient extends ProxyService, IAdaptable {

	/**
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * 
	 * @return
	 */
	boolean ping();

	/**
	 * 
	 * @param message
	 * @return
	 * @throws IOException
	 */
	String echo(String message) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	List<ExtensionItem> getExtensionItems(String platformId) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @return
	 * @throws IOException
	 */
	List<ExtensionItem> getExtensionItems(String platformId, String typeId) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws IOException
	 */
	ExtensionItem getExtensionItem(String platformId, String typeId, String extensionId) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param name
	 * @param description
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	ExtensionItem addExtensionItem(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) throws IOException;

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
	 * @throws IOException
	 */
	boolean updateExtensionItem(String platformId, String typeId, String extensionId, String newTypeId, String newExtensionId, String newName, String newDescription, Map<String, Object> properties) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	boolean removeExtensionItems(String platformId) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws IOException
	 */
	boolean removeExtensionItem(String platformId, String typeId, String extensionId) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws IOException
	 */
	Map<String, Object> getExtensionProperties(String platformId, String typeId, String extensionId) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	boolean setExtensionProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws IOException;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param propertyNames
	 * @return
	 * @throws IOException
	 */
	boolean removeExtensionProperties(String platformId, String typeId, String extensionId, List<String> propertyNames) throws IOException;

}
