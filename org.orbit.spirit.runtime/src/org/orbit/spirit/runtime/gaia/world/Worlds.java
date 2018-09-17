package org.orbit.spirit.runtime.gaia.world;

import java.util.List;

import org.orbit.spirit.runtime.gaia.service.WorldMetadata;
import org.origin.common.rest.server.ServerException;

public interface Worlds {

	List<WorldMetadata> getWorlds() throws ServerException;

	boolean exists(String name) throws ServerException;

	WorldMetadata get(String name) throws ServerException;

	WorldMetadata create(String name) throws ServerException;

	boolean delete(String name) throws ServerException;

}
