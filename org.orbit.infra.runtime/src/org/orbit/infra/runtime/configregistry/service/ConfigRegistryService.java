package org.orbit.infra.runtime.configregistry.service;

import java.util.Map;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface ConfigRegistryService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAwareService {

	ConfigRegistry[] getList() throws ServerException;

	ConfigRegistry[] getList(String type) throws ServerException;

	ConfigRegistry getById(String id) throws ServerException;

	ConfigRegistry getByName(String fullName) throws ServerException;

	boolean existsById(String id) throws ServerException;

	boolean existsByName(String fullName) throws ServerException;

	ConfigRegistry create(String type, String fullName, Map<String, Object> properties) throws ServerException;

	boolean updateType(String id, String type) throws ServerException;

	boolean updateName(String id, String fullName) throws ServerException;

	boolean updateProperties(String id, Map<String, Object> properties) throws ServerException;

	boolean deleteById(String id) throws ServerException;

	boolean deleteByName(String fullName) throws ServerException;

}
