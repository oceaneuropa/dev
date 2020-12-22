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

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ConfigRegistryUtil {

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public static ConfigRegistryClient getClient(String accessToken) {
		String url = InfraConfigPropertiesHandler.getInstance().getConfigRegistryURL();
		ConfigRegistryClient configRegistryClient = InfraClients.getInstance().getConfigRegistryClient(url, accessToken);
		return configRegistryClient;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @return
	 */
	public static ConfigRegistryClient getClient(ConfigRegistryClientResolver resolver, String accessToken) {
		ConfigRegistryClient client = null;
		if (resolver != null) {
			client = resolver.resolve(accessToken);
		}
		return client;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @return
	 * @throws ClientException
	 */
	public static ServiceMetadata getServiceMetadata(ConfigRegistryClientResolver resolver, String accessToken) throws ClientException {
		ServiceMetadata metadata = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
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
	 * @param resolver
	 * @param accessToken
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry[] getConfigRegistries(ConfigRegistryClientResolver resolver, String accessToken) throws ClientException {
		ConfigRegistry[] configRegistries = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configRegistries = client.getConfigRegistries();
		}
		if (configRegistries == null) {
			configRegistries = new ConfigRegistry[0];
		}
		return configRegistries;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry[] getConfigRegistries(ConfigRegistryClientResolver resolver, String accessToken, String type) throws ClientException {
		ConfigRegistry[] configRegistries = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configRegistries = client.getConfigRegistries(type);
		}
		if (configRegistries == null) {
			configRegistries = new ConfigRegistry[0];
		}
		return configRegistries;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry getConfigRegistryById(ConfigRegistryClientResolver resolver, String accessToken, String id) throws ClientException {
		ConfigRegistry configRegistry = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configRegistry = client.getConfigRegistryById(id);
		}
		return configRegistry;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry getConfigRegistryByName(ConfigRegistryClientResolver resolver, String accessToken, String name) throws ClientException {
		ConfigRegistry configRegistry = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configRegistry = client.getConfigRegistryByName(name);
		}
		return configRegistry;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	public static boolean configRegistryExistsById(ConfigRegistryClientResolver resolver, String accessToken, String id) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			exists = client.configRegistryExistsById(id);
		}
		return exists;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean configRegistryExistsByName(ConfigRegistryClientResolver resolver, String accessToken, String name) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			exists = client.configRegistryExistsByName(name);
		}
		return exists;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param type
	 * @param name
	 * @param properties
	 * @param generateUniqueName
	 * @return
	 * @throws ClientException
	 */
	public static ConfigRegistry createConfigRegistry(ConfigRegistryClientResolver resolver, String accessToken, String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws ClientException {
		ConfigRegistry configRegistry = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configRegistry = client.createConfigRegistry(type, name, properties, generateUniqueName);
		}
		return configRegistry;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param id
	 * @param type
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateConfigRegistryType(ConfigRegistryClientResolver resolver, String accessToken, String id, String type) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			isUpdated = client.updateConfigRegistryType(id, type);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param id
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateConfigRegistryName(ConfigRegistryClientResolver resolver, String accessToken, String id, String name) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			isUpdated = client.updateConfigRegistryName(id, name);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param oldName
	 * @param name
	 * @param value
	 * @return
	 * @throws ClientException
	 */
	public static boolean setConfigRegistryProperty(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String oldName, String name, Object value) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			succeed = client.setConfigRegistryProperty(configRegistryId, oldName, name, value);
		}
		return succeed;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param id
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public static boolean setConfigRegistryProperties(ConfigRegistryClientResolver resolver, String accessToken, String id, Map<String, Object> properties) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			isUpdated = client.setConfigRegistryProperties(id, properties);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param id
	 * @param propertyNames
	 * @return
	 * @throws ClientException
	 */
	public static boolean removeConfigRegistryProperties(ConfigRegistryClientResolver resolver, String accessToken, String id, List<String> propertyNames) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			isUpdated = client.removeConfigRegistryProperties(id, propertyNames);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteConfigRegistryById(ConfigRegistryClientResolver resolver, String accessToken, String id) throws ClientException {
		boolean isDeleted = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			isDeleted = client.deleteConfigRegistryById(id);
		}
		return isDeleted;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteConfigRegistryByName(ConfigRegistryClientResolver resolver, String accessToken, String name) throws ClientException {
		boolean isDeleted = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
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
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement[] listRootElements(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId) throws ClientException {
		ConfigElement[] configElements = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElements = client.listRootElements(configRegistryId);
		}
		if (configElements == null) {
			configElements = new ConfigElement[0];
		}
		return configElements;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement[] listElements(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String parentElementId) throws ClientException {
		ConfigElement[] configElements = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElements = client.listElements(configRegistryId, parentElementId);
		}
		if (configElements == null) {
			configElements = new ConfigElement[0];
		}
		return configElements;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentPath
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement[] listElements(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, Path parentPath) throws ClientException {
		ConfigElement[] configElements = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElements = client.listElements(configRegistryId, parentPath);
		}
		if (configElements == null) {
			configElements = new ConfigElement[0];
		}
		return configElements;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement getElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElement = client.getElement(configRegistryId, elementId);
		}
		return configElement;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement getElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, Path path) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElement = client.getElement(configRegistryId, path);
		}
		return configElement;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement getElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String parentElementId, String name) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElement = client.getElement(configRegistryId, parentElementId, name);
		}
		return configElement;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement getElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String parentElementId, Path path) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElement = client.getElement(configRegistryId, parentElementId, path);
		}
		return configElement;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @return
	 * @throws ClientException
	 */
	public static Path getElementPath(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
		Path path = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			path = client.getElementPath(configRegistryId, elementId);
		}
		return path;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @return
	 * @throws ClientException
	 */
	public static boolean elementExists(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			exists = client.elementExists(configRegistryId, elementId);
		}
		return exists;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public static boolean elementExists(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, Path path) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			exists = client.elementExists(configRegistryId, path);
		}
		return exists;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public static boolean elementExists(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String parentElementId, String name) throws ClientException {
		boolean exists = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			exists = client.elementExists(configRegistryId, parentElementId, name);
		}
		return exists;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param path
	 * @param attributes
	 * @param generateUniqueName
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement createElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElement = client.createElement(configRegistryId, path, attributes, generateUniqueName);
		}
		return configElement;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @param name
	 * @param attributes
	 * @param generateUniqueName
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement createElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElement = client.createElement(configRegistryId, parentElementId, name, attributes, generateUniqueName);
		}
		return configElement;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param parentElementId
	 * @param path
	 * @param attributes
	 * @param generateUniqueName
	 * @return
	 * @throws ClientException
	 */
	public static ConfigElement createElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String parentElementId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
		ConfigElement configElement = null;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			configElement = client.createElement(configRegistryId, parentElementId, path, attributes, generateUniqueName);
		}
		return configElement;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param newName
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateElementName(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId, String newName) throws ClientException {
		boolean isUpdated = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			isUpdated = client.updateElementName(configRegistryId, elementId, newName);
		}
		return isUpdated;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param oldAttributeName
	 * @param attributeName
	 * @param attributeValue
	 * @return
	 * @throws ClientException
	 */
	public static boolean setElementAttribute(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId, String oldAttributeName, String attributeName, Object attributeValue) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			succeed = client.setElementAttribute(configRegistryId, elementId, oldAttributeName, attributeName, attributeValue);
		}
		return succeed;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param attributes
	 * @return
	 * @throws ClientException
	 */
	public static boolean setElementAttributes(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId, Map<String, Object> attributes) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			succeed = client.setElementAttributes(configRegistryId, elementId, attributes);
		}
		return succeed;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param attributeName
	 * @return
	 * @throws ClientException
	 */
	public static boolean removeElementAttribute(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId, String attributeName) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			succeed = client.removeElementAttribute(configRegistryId, elementId, attributeName);
		}
		return succeed;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @param attributeNames
	 * @return
	 * @throws ClientException
	 */
	public static boolean removeElementAttributes(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId, List<String> attributeNames) throws ClientException {
		boolean succeed = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			succeed = client.removeElementAttributes(configRegistryId, elementId, attributeNames);
		}
		return succeed;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param elementId
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, String elementId) throws ClientException {
		boolean isDeleted = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			isDeleted = client.deleteElement(configRegistryId, elementId);
		}
		return isDeleted;
	}

	/**
	 * 
	 * @param resolver
	 * @param accessToken
	 * @param configRegistryId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteElement(ConfigRegistryClientResolver resolver, String accessToken, String configRegistryId, Path path) throws ClientException {
		boolean isDeleted = false;
		ConfigRegistryClient client = getClient(resolver, accessToken);
		if (client != null) {
			isDeleted = client.deleteElement(configRegistryId, path);
		}
		return isDeleted;
	}

}
