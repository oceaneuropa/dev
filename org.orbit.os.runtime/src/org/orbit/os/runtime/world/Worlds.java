package org.orbit.os.runtime.world;

import java.util.List;

import org.spirit.world.api.World;

public interface Worlds {

	List<World> getWorld() throws WorldException;

	boolean exists(String name) throws WorldException;

	World get(String name) throws WorldException;

	World create(String name) throws WorldException;

	boolean delete(String name) throws WorldException;

}
