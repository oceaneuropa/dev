package org.orbit.infra.runtime.configregistry.service;

import java.util.Map;

import org.origin.common.jdbc.ConnectionProvider;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ConfigRegistryService extends IWebService, ConnectionProvider, AccessTokenProvider {

	ConfigRegistry[] getConfigRegistries() throws ServerException;

	ConfigRegistry[] getConfigRegistries(String type) throws ServerException;

	ConfigRegistry getConfigRegistryById(String id) throws ServerException;

	ConfigRegistry getConfigRegistryByName(String fullName) throws ServerException;

	boolean configRegistryExistsById(String id) throws ServerException;

	boolean configRegistryExistsByName(String fullName) throws ServerException;

	ConfigRegistry createConfigRegistry(String type, String fullName, Map<String, Object> properties) throws ServerException;

	boolean updateConfigRegistryType(String id, String type) throws ServerException;

	boolean updateConfigRegistryName(String id, String fullName) throws ServerException;

	boolean updateConfigRegistryProperties(String id, Map<String, Object> properties) throws ServerException;

	boolean deleteConfigRegistryById(String id) throws ServerException;

	boolean deleteConfigRegistryByName(String fullName) throws ServerException;

}
