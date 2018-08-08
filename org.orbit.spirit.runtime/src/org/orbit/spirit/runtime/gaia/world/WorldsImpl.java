package org.orbit.spirit.runtime.gaia.world;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.rest.server.ServerException;

public class WorldsImpl implements Worlds {

	protected List<World> worlds = new ArrayList<World>();

	public WorldsImpl() {
		this.worlds = new ArrayList<World>();
	}

	@Override
	public List<World> getWorlds() throws ServerException {
		return this.worlds;
	}

	@Override
	public boolean exists(String name) throws ServerException {
		if (name != null) {
			for (World currWorld : this.worlds) {
				if (name.equals(currWorld.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public World get(String name) throws ServerException {
		World world = null;
		if (name != null) {
			for (World currWorld : this.worlds) {
				if (name.equals(currWorld.getName())) {
					world = currWorld;
					break;
				}
			}
		}
		return world;
	}

	@Override
	public synchronized World create(String name) throws ServerException {
		if (name == null || name.isEmpty()) {
			throw new ServerException("400", "World name is empty.");
		}
		if (exists(name)) {
			throw new ServerException("400", "World already exists.");
		}

		WorldImpl world = new WorldImpl();
		world.setName(name);
		this.worlds.add(world);

		return world;
	}

	@Override
	public synchronized boolean delete(String name) throws ServerException {
		boolean succeed = false;
		World worldToDelete = null;
		if (name != null) {
			for (World currWorld : this.worlds) {
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
