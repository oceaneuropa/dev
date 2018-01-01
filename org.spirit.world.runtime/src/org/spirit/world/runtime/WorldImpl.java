package org.spirit.world.runtime;

import org.spirit.world.api.World;
import org.spirit.world.api.WorldInput;

public class WorldImpl implements World {

	protected RUNTIME_STATE runtimeState = RUNTIME_STATE.STOPPED;
	protected String name;
	protected WorldInput input;

	public WorldImpl() {
	}

	public WorldImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WorldInput getInput() {
		return input;
	}

	public void setInput(WorldInput input) {
		this.input = input;
	}

	@Override
	public String toString() {
		return "World (name = '" + name + "')";
	}

	@Override
	public void start() {

	}

	@Override
	public void shutdown() {

	}

	@Override
	public RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	protected void setRuntimeState(RUNTIME_STATE runtimeState) {
		this.runtimeState = runtimeState;
	}

}
