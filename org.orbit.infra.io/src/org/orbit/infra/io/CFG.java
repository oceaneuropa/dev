package org.orbit.infra.io;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.rest.model.ServiceMetadata;

/**
 * Config Registries client API
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface CFG {

	String getAccessToken();

	ConfigRegistryClientResolver getClientResolver();

	ServiceClient getServiceClient();

	boolean isOnline();

	ServiceMetadata getServiceMetadata() throws IOException;

	IConfigRegistry[] getConfigRegistries() throws IOException;

	IConfigRegistry[] getConfigRegistries(String type) throws IOException;

	IConfigRegistry getConfigRegistryById(String id) throws IOException;

	IConfigRegistry getConfigRegistryByName(String name) throws IOException;

	boolean configRegistryExistsById(String id) throws IOException;

	boolean configRegistryExistsByName(String name) throws IOException;

	IConfigRegistry createConfigRegistry(String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws IOException;

	boolean updateConfigRegistryType(String id, String type) throws IOException;

	boolean updateConfigRegistryName(String id, String name) throws IOException;

	boolean setConfigRegistryProperty(String id, String oldName, String name, Object value) throws IOException;

	boolean setConfigRegistryProperties(String id, Map<String, Object> properties) throws IOException;

	boolean removeConfigRegistryProperties(String id, List<String> propertyNames) throws IOException;

	boolean deleteConfigRegistryById(String id) throws IOException;

	boolean deleteConfigRegistryByName(String name) throws IOException;

}
