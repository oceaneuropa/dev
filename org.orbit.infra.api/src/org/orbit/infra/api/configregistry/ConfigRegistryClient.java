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

	boolean setConfigRegistryProperty(String configRegistryId, String oldName, String name, Object value) throws ClientException;

	boolean setConfigRegistryProperties(String id, Map<String, Object> properties) throws ClientException;

	boolean removeConfigRegistryProperties(String id, List<String> propertyNames) throws ClientException;

	boolean deleteConfigRegistryById(String id) throws ClientException;

	boolean deleteConfigRegistryByName(String name) throws ClientException;

	// -----------------------------------------------------------------------------------
	// Config Elements
	// -----------------------------------------------------------------------------------
	ConfigElement[] listRootElements(String configRegistryId) throws ClientException;

	ConfigElement[] listElements(String configRegistryId, String parentElementId) throws ClientException;

	ConfigElement[] listElements(String configRegistryId, Path parentPath) throws ClientException;

	ConfigElement getElement(String configRegistryId, String elementId) throws ClientException;

	ConfigElement getElement(String configRegistryId, Path path) throws ClientException;

	ConfigElement getElement(String configRegistryId, String parentElementId, String name) throws ClientException;

	ConfigElement getElement(String configRegistryId, String parentElementId, Path path) throws ClientException;

	Path getElementPath(String configRegistryId, String elementId) throws ClientException;

	boolean elementExists(String configRegistryId, String elementId) throws ClientException;

	boolean elementExists(String configRegistryId, Path path) throws ClientException;

	boolean elementExists(String configRegistryId, String parentElementId, String name) throws ClientException;

	ConfigElement createElement(String configRegistryId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException;

	ConfigElement createElement(String configRegistryId, String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException;

	ConfigElement createElement(String configRegistryId, String parentElementId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException;

	boolean updateElementName(String configRegistryId, String elementId, String newName) throws ClientException;

	boolean setElementAttribute(String configRegistryId, String elementId, String oldAttributeName, String attributeName, Object attributeValue) throws ClientException;

	boolean setElementAttributes(String configRegistryId, String elementId, Map<String, Object> attributes) throws ClientException;

	boolean removeElementAttribute(String configRegistryId, String elementId, String attributeName) throws ClientException;

	boolean removeElementAttributes(String configRegistryId, String elementId, List<String> attributeNames) throws ClientException;

	boolean deleteElement(String configRegistryId, String elementId) throws ClientException;

	boolean deleteElement(String configRegistryId, Path path) throws ClientException;

}
