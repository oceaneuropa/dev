package org.orbit.infra.api.util;

import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.ServiceMetadata;

public class ConfigRegistryUtil {

	protected static ConfigRegistry[] EMPTY_CONFIG_REGISTRIES = new ConfigRegistry[0];
	protected static ConfigElement[] EMPTY_CONFIG_ELEMENTS = new ConfigElement[0];

	/**
	 * 
	 * @param configRegistryServiceUrl
	 * @param accessToken
	 * @return
	 */
	public static ConfigRegistryClient getClient(String accessToken) {
		String configRegistryServiceUrl = InfraServicesPropertiesHandler.getInstance().getConfigRegistryURL();
		ConfigRegistryClient configRegistryClient = InfraClients.getInstance().getConfigRegistryClient(configRegistryServiceUrl, accessToken);
		return configRegistryClient;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param configRegistryServiceUrl
	 * @param accessToken
	 * @return
	 */
	public static ConfigRegistryClient getClient(ConfigRegistryClientResolver clientResolver, String accessToken) {
		ConfigRegistryClient configRegistryClient = null;
		if (clientResolver != null) {
			configRegistryClient = clientResolver.resolve(accessToken);
		}
		return configRegistryClient;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @return
	 * @throws ClientException
	 */
	public static ServiceMetadata getServiceMetadata(ConfigRegistryClientResolver clientResolver, String accessToken) throws ClientException {
		ServiceMetadata metadata = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			metadata = client.getMetadata();
		}
		return metadata;
	}

	// -----------------------------------------------------------------------------------
	// Config Registries
	// -----------------------------------------------------------------------------------
	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry[] getConfigRegistries(ConfigRegistryClientResolver clientResolver, String accessToken) throws ClientException {
		ConfigRegistry[] configRegistries = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configRegistries = client.getConfigRegistries();
		}
		if (configRegistries == null) {
			configRegistries = EMPTY_CONFIG_REGISTRIES;
		}
		return configRegistries;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry[] getConfigRegistries(ConfigRegistryClientResolver clientResolver, String accessToken, String type) throws ClientException {
		ConfigRegistry[] configRegistries = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configRegistries = client.getConfigRegistries(type);
		}
		if (configRegistries == null) {
			configRegistries = EMPTY_CONFIG_REGISTRIES;
		}
		return configRegistries;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry getConfigRegistryById(ConfigRegistryClientResolver clientResolver, String accessToken, String id) throws ClientException {
		ConfigRegistry configRegistry = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configRegistry = client.getConfigRegistryById(id);
		}
		return configRegistry;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry getConfigRegistryByName(ConfigRegistryClientResolver clientResolver, String accessToken, String name) throws ClientException {
		ConfigRegistry configRegistry = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configRegistry = client.getConfigRegistryByName(name);
		}
		return configRegistry;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	public static boolean configRegistryExistsById(ConfigRegistryClientResolver clientResolver, String accessToken, String id) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			exists = client.configRegistryExistsById(id);
		}
		return exists;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean configRegistryExistsByName(ConfigRegistryClientResolver clientResolver, String accessToken, String name) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			exists = client.configRegistryExistsByName(name);
		}
		return exists;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param type
	 * @param name
	 * @param properties
	 * @param generateUniqueName
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry createConfigRegistry(ConfigRegistryClientResolver clientResolver, String accessToken, String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws ClientException {
		ConfigRegistry configRegistry = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configRegistry = client.createConfigRegistry(type, name, properties, generateUniqueName);
		}
		return configRegistry;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param id
	 * @param type
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateConfigRegistryType(ConfigRegistryClientResolver clientResolver, String accessToken, String id, String type) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isUpdated = client.updateConfigRegistryType(id, type);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param id
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateConfigRegistryName(ConfigRegistryClientResolver clientResolver, String accessToken, String id, String name) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isUpdated = client.updateConfigRegistryName(id, name);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param oldName
	 * @param name
	 * @param value
	 * @return
	 * @throws ClientException
	 */
	public static boolean setConfigRegistryProperty(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String oldName, String name, Object value) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			succeed = client.setConfigRegistryProperty(configRegistryId, oldName, name, value);
		}
		return succeed;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param id
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public static boolean setConfigRegistryProperties(ConfigRegistryClientResolver clientResolver, String accessToken, String id, Map<String, Object> properties) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isUpdated = client.setConfigRegistryProperties(id, properties);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param id
	 * @param propertyNames
	 * @return
	 * @throws ClientException
	 */
	public static boolean removeConfigRegistryProperties(ConfigRegistryClientResolver clientResolver, String accessToken, String id, List<String> propertyNames) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isUpdated = client.removeConfigRegistryProperties(id, propertyNames);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteConfigRegistryById(ConfigRegistryClientResolver clientResolver, String accessToken, String id) throws ClientException {
		boolean isDeleted = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isDeleted = client.deleteConfigRegistryById(id);
		}
		return isDeleted;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteConfigRegistryByName(ConfigRegistryClientResolver clientResolver, String accessToken, String name) throws ClientException {
		boolean isDeleted = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isDeleted = client.deleteConfigRegistryByName(name);
		}
		return isDeleted;
	}

	// -----------------------------------------------------------------------------------
	// Config Elements
	// -----------------------------------------------------------------------------------
	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement[] listRootConfigElements(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId) throws ClientException {
		ConfigElement[] configElements = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configElements = client.listRootConfigElements(configRegistryId);
		}
		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
		}
		return configElements;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement[] listConfigElements(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String parentElementId) throws ClientException {
		ConfigElement[] configElements = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configElements = client.listConfigElements(configRegistryId, parentElementId);
		}
		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
		}
		return configElements;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentPath
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement[] listConfigElements(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path parentPath) throws ClientException {
		ConfigElement[] configElements = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configElements = client.listConfigElements(configRegistryId, parentPath);
		}
		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
		}
		return configElements;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configElement = client.getConfigElement(configRegistryId, elementId);
		}
		return configElement;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path path) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configElement = client.getConfigElement(configRegistryId, path);
		}
		return configElement;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement getConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String parentElementId, String name) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configElement = client.getConfigElement(configRegistryId, parentElementId, name);
		}
		return configElement;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @return
	 * @throws ClientException
	 */
	public static Path getConfigElementPath(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
		Path path = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			path = client.getConfigElementPath(configRegistryId, elementId);
		}
		return path;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @return
	 * @throws ClientException
	 */
	public static boolean configElementExists(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			exists = client.configElementExists(configRegistryId, elementId);
		}
		return exists;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public static boolean configElementExists(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path path) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			exists = client.configElementExists(configRegistryId, path);
		}
		return exists;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean configElementExists(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String parentElementId, String name) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			exists = client.configElementExists(configRegistryId, parentElementId, name);
		}
		return exists;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param path
	 * @param attributes
	 * @param generateUniqueName
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement createConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configElement = client.createConfigElement(configRegistryId, path, attributes, generateUniqueName);
		}
		return configElement;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @param name
	 * @param attributes
	 * @param generateUniqueName
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement createConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			configElement = client.createConfigElement(configRegistryId, parentElementId, name, attributes, generateUniqueName);
		}
		return configElement;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param newName
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateConfigElementName(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, String newName) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isUpdated = client.updateConfigElementName(configRegistryId, elementId, newName);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param oldAttributeName
	 * @param attributeName
	 * @param attributeValue
	 * @return
	 * @throws ClientException
	 */
	public static boolean setConfigElementAttribute(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, String oldAttributeName, String attributeName, Object attributeValue) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			succeed = client.setConfigElementAttribute(configRegistryId, elementId, oldAttributeName, attributeName, attributeValue);
		}
		return succeed;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param attributes
	 * @return
	 * @throws ClientException
	 */
	public static boolean setConfigElementAttributes(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, Map<String, Object> attributes) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			succeed = client.setConfigElementAttributes(configRegistryId, elementId, attributes);
		}
		return succeed;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param attributeName
	 * @return
	 * @throws ClientException
	 */
	public static boolean removeConfigElementAttribute(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, String attributeName) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			succeed = client.removeConfigElementAttribute(configRegistryId, elementId, attributeName);
		}
		return succeed;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param attributeNames
	 * @return
	 * @throws ClientException
	 */
	public static boolean removeConfigElementAttributes(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId, List<String> attributeNames) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			succeed = client.removeConfigElementAttributes(configRegistryId, elementId, attributeNames);
		}
		return succeed;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
		boolean isDeleted = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isDeleted = client.deleteConfigElement(configRegistryId, elementId);
		}
		return isDeleted;
	}

	/**
	 * 
	 * @param clientResolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteConfigElement(ConfigRegistryClientResolver clientResolver, String accessToken, String configRegistryId, Path path) throws ClientException {
		boolean isDeleted = false;
		ConfigRegistryClient client = getClient(clientResolver, accessToken);
		if (client != null) {
			isDeleted = client.deleteConfigElement(configRegistryId, path);
		}
		return isDeleted;
	}

}

// /**
// *
// * @param configRegistryServiceUrl
// * @param accessToken
// * @return
// */
// protected ConfigRegistryClient getConfigRegistryClient(String configRegistryServiceUrl, String accessToken) {
// ConfigRegistryClient configRegistryClient = InfraClients.getInstance().getConfigRegistryClient(configRegistryServiceUrl, accessToken);
// return configRegistryClient;
// }
