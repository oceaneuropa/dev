package org.orbit.infra.connector.configregistry;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.connector.util.ClientModelConverter;
import org.orbit.infra.model.RequestConstants;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.ResponseUtil;

public class ConfigRegistryClientImpl extends ServiceClientImpl<ConfigRegistryClient, ConfigRegistryWSClient> implements ConfigRegistryClient {

	private static final ConfigRegistry[] EMPTY_CONFIG_REGISTRIES = new ConfigRegistry[0];
	private static final ConfigElement[] EMPTY_CONFIG_ELEMENTS = new ConfigElement[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public ConfigRegistryClientImpl(ServiceConnector<ConfigRegistryClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected ConfigRegistryWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration clientConfig = WSClientConfiguration.create(properties);
		return new ConfigRegistryWSClient(clientConfig);
	}

	@Override
	public ConfigRegistry[] getConfigRegistries() throws ClientException {
		Request request = new Request(RequestConstants.CONFIG_REGISTRY__LIST_CONFIG_REGISTRIES);

		ConfigRegistry[] configRegistries = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configRegistries = ClientModelConverter.CONFIG_REGISTRY.getConfigRegistries(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (configRegistries == null) {
			configRegistries = EMPTY_CONFIG_REGISTRIES;
		}
		return configRegistries;
	}

	@Override
	public ConfigRegistry[] getConfigRegistries(String type) throws ClientException {
		checkNullParameter(type, "type is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__LIST_CONFIG_REGISTRIES);
		request.setParameter("type", type);

		ConfigRegistry[] configRegistries = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configRegistries = ClientModelConverter.CONFIG_REGISTRY.getConfigRegistries(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (configRegistries == null) {
			configRegistries = EMPTY_CONFIG_REGISTRIES;
		}
		return configRegistries;
	}

	@Override
	public ConfigRegistry getConfigRegistryById(String id) throws ClientException {
		checkNullParameter(id, "id is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__GET_CONFIG_REGISTRY);
		request.setParameter("id", id);

		ConfigRegistry configRegistry = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configRegistry = ClientModelConverter.CONFIG_REGISTRY.getConfigRegistry(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return configRegistry;
	}

	@Override
	public ConfigRegistry getConfigRegistryByName(String fullName) throws ClientException {
		checkNullParameter(fullName, "fullName is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__GET_CONFIG_REGISTRY);
		request.setParameter("name", fullName);

		ConfigRegistry configRegistry = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configRegistry = ClientModelConverter.CONFIG_REGISTRY.getConfigRegistry(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return configRegistry;
	}

	@Override
	public boolean configRegistryExistsById(String id) throws ClientException {
		checkNullParameter(id, "id is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__CONFIG_REGISTRY_EXISTS);
		request.setParameter("id", id);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ClientModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean configRegistryExistsByName(String fullName) throws ClientException {
		checkNullParameter(fullName, "fullName is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__CONFIG_REGISTRY_EXISTS);
		request.setParameter("name", fullName);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ClientModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public ConfigRegistry createConfigRegistry(String type, String fullName, Map<String, Object> properties, boolean generateUniqueName) throws ClientException {
		checkNullParameter(type, "type is null.");
		checkNullParameter(fullName, "fullName is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__CREATE_CONFIG_REGISTRY);
		request.setParameter("type", type);
		request.setParameter("name", fullName);
		if (properties != null) {
			request.setParameter("properties", properties);
		}
		request.setParameter("generate_unique_name", generateUniqueName);

		ConfigRegistry configRegistry = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configRegistry = ClientModelConverter.CONFIG_REGISTRY.getConfigRegistry(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return configRegistry;
	}

	@Override
	public boolean updateConfigRegistryType(String id, String type) throws ClientException {
		checkNullParameter(id, "id is null.");
		checkNullParameter(type, "type is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__UPDATE_CONFIG_REGISTRY);
		request.setParameter("id", id);
		request.setParameter("type", type);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateConfigRegistryName(String id, String fullName) throws ClientException {
		checkNullParameter(id, "id is null.");
		checkNullParameter(fullName, "fullName is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__UPDATE_CONFIG_REGISTRY);
		request.setParameter("id", id);
		request.setParameter("name", fullName);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean setConfigRegistryProperties(String id, Map<String, Object> properties) throws ClientException {
		checkNullParameter(id, "id is null.");
		checkNullParameter(properties, "properties is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__SET_CONFIG_REGISTRY_PROPERTIES);
		request.setParameter("id", id);
		request.setParameter("properties", properties);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean removeConfigRegistryProperties(String id, List<String> propertyNames) throws ClientException {
		checkNullParameter(id, "id is null.");
		checkNullParameter(propertyNames, "propertyNames is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__REMOVE_CONFIG_REGISTRY_PROPERTIES);
		request.setParameter("id", id);
		request.setParameter("property_names", propertyNames);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteConfigRegistryById(String id) throws ClientException {
		checkNullParameter(id, "id is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__DELETE_CONFIG_REGISTRY);
		request.setParameter("id", id);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteConfigRegistryByName(String fullName) throws ClientException {
		checkNullParameter(fullName, "fullName is null.");

		Request request = new Request(RequestConstants.CONFIG_REGISTRY__DELETE_CONFIG_REGISTRY);
		request.setParameter("name", fullName);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public ConfigElement[] listRootConfigElements(String configRegistryId) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__LIST_CONFIG_ELEMENTS);
		request.setParameter("config_registry_id", configRegistryId);

		ConfigElement[] configElements = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configElements = ClientModelConverter.CONFIG_REGISTRY.getConfigElements(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
		}
		return configElements;
	}

	@Override
	public ConfigElement[] listConfigElements(String configRegistryId, String parentElementId) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(parentElementId, "parentElementId is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__LIST_CONFIG_ELEMENTS);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("parent_element_id", parentElementId);

		ConfigElement[] configElements = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configElements = ClientModelConverter.CONFIG_REGISTRY.getConfigElements(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
		}
		return configElements;
	}

	@Override
	public ConfigElement[] listConfigElements(String configRegistryId, Path parentPath) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(parentPath, "parentPath is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__LIST_CONFIG_ELEMENTS);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("parent_element_path", parentPath.getPathString());

		ConfigElement[] configElements = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configElements = ClientModelConverter.CONFIG_REGISTRY.getConfigElements(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
		}
		return configElements;
	}

	@Override
	public ConfigElement getConfigElement(String configRegistryId, String elementId) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__GET_CONFIG_ELEMENT);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);

		ConfigElement configElement = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configElement = ClientModelConverter.CONFIG_REGISTRY.getConfigElement(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return configElement;
	}

	@Override
	public ConfigElement getConfigElement(String configRegistryId, Path path) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(path, "path is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__GET_CONFIG_ELEMENT);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_path", path.getPathString());

		ConfigElement configElement = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configElement = ClientModelConverter.CONFIG_REGISTRY.getConfigElement(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return configElement;
	}

	@Override
	public ConfigElement getConfigElement(String configRegistryId, String parentElementId, String name) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(name, "name is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__GET_CONFIG_ELEMENT);
		request.setParameter("config_registry_id", configRegistryId);
		if (parentElementId != null) {
			request.setParameter("parent_element_id", parentElementId);
		}
		request.setParameter("element_name", name);

		ConfigElement configElement = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configElement = ClientModelConverter.CONFIG_REGISTRY.getConfigElement(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return configElement;
	}

	@Override
	public Path getConfigElementPath(String configRegistryId, String elementId) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__GET_CONFIG_ELEMENT_PATH);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);

		Path path = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				path = ClientModelConverter.COMMON.getPath(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return path;
	}

	@Override
	public boolean configElementExists(String configRegistryId, String elementId) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__CONFIG_ELEMENT_EXISTS);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ClientModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean configElementExists(String configRegistryId, Path path) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(path, "path is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__CONFIG_ELEMENT_EXISTS);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_path", path.getPathString());

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ClientModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean configElementExists(String configRegistryId, String parentElementId, String name) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(name, "name is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__CONFIG_ELEMENT_EXISTS);
		request.setParameter("config_registry_id", configRegistryId);
		if (parentElementId != null) {
			request.setParameter("parent_element_id", parentElementId);
		}
		request.setParameter("element_name", name);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ClientModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public ConfigElement createConfigElement(String configRegistryId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(path, "path is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__CREATE_CONFIG_ELEMENT);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_path", path.getPathString());
		if (attributes != null) {
			request.setParameter("attributes", attributes);
		}
		request.setParameter("generate_unique_name", generateUniqueName);

		ConfigElement configElement = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configElement = ClientModelConverter.CONFIG_REGISTRY.getConfigElement(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return configElement;
	}

	@Override
	public ConfigElement createConfigElement(String configRegistryId, String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(name, "name is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__CREATE_CONFIG_ELEMENT);
		request.setParameter("config_registry_id", configRegistryId);
		if (parentElementId != null) {
			request.setParameter("parent_element_id", parentElementId);
		}
		request.setParameter("element_name", name);
		if (attributes != null) {
			request.setParameter("attributes", attributes);
		}
		request.setParameter("generate_unique_name", generateUniqueName);

		ConfigElement configElement = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				configElement = ClientModelConverter.CONFIG_REGISTRY.getConfigElement(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return configElement;
	}

	@Override
	public boolean updateConfigElementName(String configRegistryId, String elementId, String newName) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");
		checkNullParameter(newName, "newName is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__UPDATE_CONFIG_ELEMENT);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);
		request.setParameter("element_name", newName);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean setConfigElementAttribute(String configRegistryId, String elementId, String oldAttributeName, String attributeName, Object attributeValue) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");
		checkNullParameter(attributeName, "attribute name is null.");
		checkNullParameter(attributeValue, "attribute value is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__SET_CONFIG_ELEMENT_ATTRIBUTE);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);
		request.setParameter("old_attribute_name", oldAttributeName);
		request.setParameter("attribute_name", attributeName);
		request.setParameter("attribute_value", attributeValue);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean setConfigElementAttributes(String configRegistryId, String elementId, Map<String, Object> attributes) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");
		checkNullParameter(attributes, "attributes is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__SET_CONFIG_ELEMENT_ATTRIBUTES);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);
		request.setParameter("attributes", attributes);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean removeConfigElementAttribute(String configRegistryId, String elementId, String attributeName) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");
		checkNullParameter(attributeName, "attribute name is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__REMOVE_CONFIG_ELEMENT_ATTRIBUTE);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);
		request.setParameter("attribute_name", attributeName);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean removeConfigElementAttributes(String configRegistryId, String elementId, List<String> attributeNames) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");
		checkNullParameter(attributeNames, "attributeName is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__REMOVE_CONFIG_ELEMENT_ATTRIBUTES);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);
		request.setParameter("attribute_names", attributeNames);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteConfigElement(String configRegistryId, String elementId) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(elementId, "elementId is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__DELETE_CONFIG_ELEMENT);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_id", elementId);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteConfigElement(String configRegistryId, Path path) throws ClientException {
		checkNullParameter(configRegistryId, "configRegistryId is null.");
		checkNullParameter(path, "path is null.");

		Request request = new Request(RequestConstants.CONFIG_ELEMENT__DELETE_CONFIG_ELEMENT);
		request.setParameter("config_registry_id", configRegistryId);
		request.setParameter("element_path", path.getPathString());

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

}
