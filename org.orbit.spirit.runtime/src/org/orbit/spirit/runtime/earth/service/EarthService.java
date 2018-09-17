package org.orbit.spirit.runtime.earth.service;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface EarthService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAwareService {

	String getGaiaId();

	String getEarthId();

	World[] getWorlds() throws ServerException;

	boolean worldExists(String name) throws ServerException;

	World getWorld(String name) throws ServerException;

	World createWorld(String name) throws ServerException;

	boolean deleteWorld(String name) throws ServerException;

	boolean join(World world, String accountId) throws ServerException;

	boolean leave(World world, String accountId) throws ServerException;

}
