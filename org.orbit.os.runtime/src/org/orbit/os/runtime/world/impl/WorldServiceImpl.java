package org.orbit.os.runtime.world.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.os.model.gaia.rto.WorldException;
import org.orbit.os.runtime.world.World;
import org.orbit.os.runtime.world.Worlds;

public class WorldServiceImpl implements Worlds {

	protected List<World> worlds = new ArrayList<World>();

	public WorldServiceImpl() {
		this.worlds = new ArrayList<World>();
	}

	@Override
	public List<World> getWorlds() throws WorldException {
		return this.worlds;
	}

	@Override
	public boolean exists(String name) throws WorldException {
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
	public World get(String name) throws WorldException {
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
	public synchronized World create(String name) throws WorldException {
		if (name == null || name.isEmpty()) {
			throw new WorldException("400", "World name is empty.");
		}
		if (exists(name)) {
			throw new WorldException("400", "World already exists.");
		}

		WorldImpl world = new WorldImpl();
		world.setName(name);
		this.worlds.add(world);

		return world;
	}

	@Override
	public synchronized boolean delete(String name) throws WorldException {
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
