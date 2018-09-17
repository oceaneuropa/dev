package org.orbit.spirit.runtime.gaia.service;

import java.util.List;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface GaiaService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAwareService {

	String getGaiaId();

	List<WorldMetadata> getWorlds() throws ServerException;

	boolean worldExists(String name) throws ServerException;

	WorldMetadata getWorld(String name) throws ServerException;

	WorldMetadata createWorld(String name) throws ServerException;

	boolean deleteWorld(String name) throws ServerException;

}
