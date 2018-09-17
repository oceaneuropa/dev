package org.orbit.spirit.runtime.gaia.world;

import java.util.ArrayList;
import java.util.List;

import org.orbit.spirit.runtime.gaia.service.WorldMetadata;
import org.orbit.spirit.runtime.gaia.service.impl.WorldMetadataImpl;
import org.origin.common.rest.server.ServerException;

public class WorldsImpl implements Worlds {

	protected List<WorldMetadata> worlds = new ArrayList<WorldMetadata>();

	public WorldsImpl() {
		this.worlds = new ArrayList<WorldMetadata>();
	}

	@Override
	public List<WorldMetadata> getWorlds() throws ServerException {
		return this.worlds;
	}

	@Override
	public boolean exists(String name) throws ServerException {
		if (name != null) {
			for (WorldMetadata currWorld : this.worlds) {
				if (name.equals(currWorld.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public WorldMetadata get(String name) throws ServerException {
		WorldMetadata world = null;
		if (name != null) {
			for (WorldMetadata currWorld : this.worlds) {
				if (name.equals(currWorld.getName())) {
					world = currWorld;
					break;
				}
			}
		}
		return world;
	}

	@Override
	public synchronized WorldMetadata create(String name) throws ServerException {
		if (name == null || name.isEmpty()) {
			throw new ServerException("400", "World name is empty.");
		}
		if (exists(name)) {
			throw new ServerException("400", "World already exists.");
		}

		WorldMetadataImpl world = new WorldMetadataImpl();
		world.setName(name);
		this.worlds.add(world);

		return world;
	}

	@Override
	public synchronized boolean delete(String name) throws ServerException {
		boolean succeed = false;
		WorldMetadata worldToDelete = null;
		if (name != null) {
			for (WorldMetadata currWorld : this.worlds) {
				if (name.equals(currWorld.getName())) {
					worldToDelete = currWorld;
					break;
				}
			}
		}
		if (worldToDelete != null) {
			succeed = this.worlds.remove(worldToDelete);
		}
		return succeed;
	}

}
