package org.orbit.infra.api.configregistry;

import java.util.List;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.service.Proxy;

public interface ConfigRegistryClient extends ServiceClient, Proxy, IAdaptable {

	// -----------------------------------------------------------------------------------
	// Config Registries
	// -----------------------------------------------------------------------------------
	ConfigRegistry[] getConfigRegistries() throws ClientException;

	ConfigRegistry[] getConfigRegistries(String type) throws ClientException;

	ConfigRegistry getConfigRegistryById(String id) throws ClientException;

	ConfigRegistry getConfigRegistryByName(String name) throws ClientException;

	boolean configRegistryExistsById(String id) throws ClientException;

	boolean configRegistryExistsByName(String name) throws ClientException;

	ConfigRegistry createConfigRegistry(String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws ClientException;

	boolean updateConfigRegistryType(String id, String type) throws ClientException;

	boolean updateConfigRegistryName(String id, String name) throws ClientException;

	boolean setConfigRegistryProperties(String id, Map<String, Object> properties) throws ClientException;

	boolean removeConfigRegistryProperties(String id, List<String> propertyNames) throws ClientException;

	boolean deleteConfigRegistryById(String id) throws ClientException;

	boolean deleteConfigRegistryByName(String name) throws ClientException;

	// -----------------------------------------------------------------------------------
	// Config Elements
	// -----------------------------------------------------------------------------------
	ConfigElement[] listRootConfigElements(String configRegistryId) throws ClientException;

	ConfigElement[] listConfigElements(String configRegistryId, String parentElementId) throws ClientException;

	ConfigElement[] listConfigElements(String configRegistryId, Path parentPath) throws ClientException;

	ConfigElement getConfigElement(String configRegistryId, String elementId) throws ClientException;

	ConfigElement getConfigElement(String configRegistryId, Path path) throws ClientException;

	ConfigElement getConfigElement(String configRegistryId, String parentElementId, String name) throws ClientException;

	Path getConfigElementPath(String configRegistryId, String elementId) throws ClientException;

	boolean configElementExists(String configRegistryId, String elementId) throws ClientException;

	boolean configElementExists(String configRegistryId, Path path) throws ClientException;

	boolean configElementExists(String configRegistryId, String parentElementId, String name) throws ClientException;

	ConfigElement createConfigElement(String configRegistryId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException;

	ConfigElement createConfigElement(String configRegistryId, String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException;

	boolean updateConfigElementName(String configRegistryId, String elementId, String newName) throws ClientException;

	boolean setConfigElementAttributes(String configRegistryId, String elementId, Map<String, Object> attributes) throws ClientException;

	boolean removeConfigElementAttributes(String configRegistryId, String elementId, List<String> attributeName) throws ClientException;

	boolean deleteConfigElement(String configRegistryId, String elementId) throws ClientException;

	boolean deleteConfigElement(String configRegistryId, Path path) throws ClientException;

}
