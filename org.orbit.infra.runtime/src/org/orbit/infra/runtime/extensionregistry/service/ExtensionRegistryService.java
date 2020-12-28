package org.orbit.infra.runtime.extensionregistry.service;

import java.util.List;
import java.util.Map;

import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ExtensionRegistryService extends IWebService, AccessTokenProvider {

	List<ExtensionItem> getExtensionItems(String platformId) throws ServerException;

	List<ExtensionItem> getExtensionItems(String platformId, String typeId) throws ServerException;

	ExtensionItem getExtensionItem(String platformId, String typeId, String extensionId) throws ServerException;

	ExtensionItem addExtensionItem(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) throws ServerException;

	boolean updateExtensionItem(String platformId, String typeId, String extensionId, String newTypeId, String newExtensionId, String newName, String newDescription, Map<String, Object> properties) throws ServerException;

	boolean removeExtensionItems(String platformId, boolean disposeWhenEmpty) throws ServerException;

	boolean removeExtensionItem(String platformId, String typeId, String extensionId) throws ServerException;

	Map<String, Object> getProperties(String platformId, String typeId, String extensionId) throws ServerException;

	boolean setProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws ServerException;

	boolean removeProperties(String platformId, String typeId, String extensionId, List<String> propNames) throws ServerException;

}
