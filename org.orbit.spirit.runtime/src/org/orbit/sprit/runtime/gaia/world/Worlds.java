package org.orbit.sprit.runtime.gaia.world;

import java.util.List;

import org.origin.common.rest.server.ServerException;

public interface Worlds {

	List<World> getWorlds() throws ServerException;

	boolean exists(String name) throws ServerException;

	World get(String name) throws ServerException;

	World create(String name) throws ServerException;

	boolean delete(String name) throws ServerException;

}
