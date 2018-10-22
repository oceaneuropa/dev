package org.orbit.infra.runtime.configregistry.service;

import java.util.Map;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface ConfigRegistryService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAwareService {

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
